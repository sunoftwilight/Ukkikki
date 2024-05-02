import face_recognition
import numpy as np
from datetime import datetime
import cv2
import os
import shutil
import imutils
import io
from dotenv import load_dotenv
import boto3
import pymysql
import base64
import hashlib


load_dotenv()

DB_HOST = os.environ.get('DB_HOST')
DB_PORT = int(os.environ.get('DB_PORT'))
DB_USER = os.environ.get('DB_USER')
DB_PASSWORD = os.environ.get('DB_PASSWORD')
DB_NAME = os.environ.get('DB_NAME')

db = pymysql.connect(host=DB_HOST, port=DB_PORT, user=DB_USER, password=DB_PASSWORD, db=DB_NAME, charset='utf8')

ACCESS_KEY_ID = os.environ.get('S3_ACCESS_KEY')  # s3 관련 권한을 가진 IAM계정 정보
ACCESS_SECRET_KEY = os.environ.get('S3_SECRET_KEY')
BUCKET_NAME = os.environ.get('S3_BUCKET_NAME')
S3_REGION = os.environ.get('S3_REGION')

ratio = 1.0
similarity_threshold = 0.5

def generate_aes_key(input_data):
    sha256_hash = hashlib.sha256()
    sha256_hash.update(input_data.encode('utf-8'))
    return sha256_hash.digest()[:32]

def generate_key_md5(b64_key):
    key_bytes = base64.b64decode(b64_key)
    md5_hash = hashlib.md5(key_bytes).digest()
    return base64.b64encode(md5_hash).decode('utf-8')

def face_classifier(file, partyId, key) :
    # todo : file로 들어온 사진 이미지 cv2 형식으로 변환
    in_memory_file = io.BytesIO()
    file.save(in_memory_file)
    data = np.frombuffer(in_memory_file.getvalue(), dtype=np.uint8)
    color_image_flag = 1  # cv2.IMREAD_COLOR로도 설정 가능
    image = cv2.imdecode(data, color_image_flag)

    # todo : 이미지에서 얼굴 추출
    faces = detect_faces(image)
    cursor = db.cursor()
    # todo : face 이미지 s3 저장 해결 *^^*
    s3 = boto3.client('s3', aws_access_key_id=ACCESS_KEY_ID, aws_secret_access_key=ACCESS_SECRET_KEY)
    sse_key = generate_aes_key(key)
    encode = base64.b64encode(sse_key).decode('utf-8')
    md5 = generate_key_md5(encode)
    sse_conf = {"SSECustomerAlgorithm": "AES256", "SSECustomerKey": encode, "SSECustomerKeyMD5": md5, "ACL": "public-read", "ContentType": 'image/png'}

    for face in faces:
        is_success, buffer = cv2.imencode('.png', face.image)
        io_buf = io.BytesIO(buffer)
        s3.upload_fileobj(io_buf, BUCKET_NAME, face.filename, sse_conf)

    # todo : 생성된 그룹과 인코딩 값 비교

    sql = 'select face_group_number, encoding, face_id_list from face_group where party_id = %s order by face_group_number'
    cursor.execute(sql, partyId)
    face_group_list = cursor.fetchall()
    # for face in faces:
    #     if len(face_group_list) == 0:
    #         face_group_list
    #     for face_group in face_group_list:

    # todo : 생성된 그룹에 포함 된다면 그룹 아이디 설정하여 DB 저장하고 그룹 인코딩 값 업데이트

    # todo : 기존 생성된 그룹과 일치하지 않는다면 언노운과 비교하여 가장 가까운 값 찾기

    # todo : 새로운 그룹을 생성하여 DB 업데이트
def compare_with_known_persons(face, face_classes):
    if len(face_classes) == 0:
        return None

    # see if the face is a match for the faces of known person
    encodings = [np.array(fc[1]) for fc in face_classes]
    distances = face_recognition.face_distance(encodings, face.encoding)
    index = np.argmin(distances)
    min_value = distances[index]
    if min_value < similarity_threshold:
        # face of known person
        #persons[index].add_face(face)
        # re-calculate encoding
        face_classes[index].calculate_average_encoding()
        # face.name = persons[index].name
        # return persons[index]
        return None

def compare_with_unknown_faces(face, unknown_faces):
    if len(unknown_faces) == 0:
        # this is the first face
        unknown_faces.append(face)
        face.name = "unknown"
        return

    encodings = [face.encoding for face in unknown_faces]
    distances = face_recognition.face_distance(encodings, face.encoding)
    index = np.argmin(distances)
    min_value = distances[index]
    if min_value < similarity_threshold:
        # two faces are similar - create new person with two faces
        person = Person()
        newly_known_face = unknown_faces.pop(index)
        person.add_face(newly_known_face)
        person.add_face(face)
        person.calculate_average_encoding()
        face.name = person.name
        newly_known_face.name = person.name
        return person
    else:
        # unknown face
        unknown_faces.append(face)
        face.name = "unknown"
        return None

def get_face_image(frame, box):
    img_height, img_width = frame.shape[:2]
    (box_top, box_right, box_bottom, box_left) = box
    box_width = box_right - box_left
    box_height = box_bottom - box_top
    crop_top = max(box_top - box_height, 0)
    pad_top = -min(box_top - box_height, 0)
    crop_bottom = min(box_bottom + box_height, img_height - 1)
    pad_bottom = max(box_bottom + box_height - img_height, 0)
    crop_left = max(box_left - box_width, 0)
    pad_left = -min(box_left - box_width, 0)
    crop_right = min(box_right + box_width, img_width - 1)
    pad_right = max(box_right + box_width - img_width, 0)
    face_image = frame[crop_top:crop_bottom, crop_left:crop_right]
    if (pad_top == 0 and pad_bottom == 0):
        if (pad_left == 0 and pad_right == 0):
            return face_image
    padded = cv2.copyMakeBorder(face_image, pad_top, pad_bottom,
                                pad_left, pad_right, cv2.BORDER_CONSTANT)
    return padded


def locate_faces(frame):
    #start_time = time.time()
    if ratio == 1.0:
        rgb = frame[:, :, ::-1]
    else:
        small_frame = cv2.resize(frame, (0, 0), fx=ratio, fy=ratio)
        rgb = small_frame[:, :, ::-1]
    boxes = face_recognition.face_locations(rgb)
    #elapsed_time = time.time() - start_time
    #print("locate_faces takes %.3f seconds" % elapsed_time)
    if ratio == 1.0:
        return boxes
    boxes_org_size = []
    for box in boxes:
        (top, right, bottom, left) = box
        left = int(left / ratio)
        right = int(right / ratio)
        top = int(top / ratio)
        bottom = int(bottom / ratio)
        box_org_size = (top, right, bottom, left)
        boxes_org_size.append(box_org_size)
    return boxes_org_size

def detect_faces(frame):
    boxes = locate_faces(frame)
    if len(boxes) == 0:
        return []

    # faces found
    faces = []
    now = datetime.now()
    str_ms = now.strftime('%Y%m%d_%H%M%S.%f')[:-3] + '-'
    encodings = face_recognition.face_encodings(frame, boxes)
    for i, box in enumerate(boxes):
        face_image = get_face_image(frame, box)
        face = Face(str_ms + str(i) + ".png", face_image, encodings[i])
        face.location = box
        faces.append(face)
    return faces

class Face():
    key = "face_encoding"

    def __init__(self, filename, image, face_encoding):
        self.filename = filename
        self.image = image
        self.encoding = face_encoding

    def save(self, base_dir):
        # save image
        pathname = os.path.join(base_dir, self.filename)
        print(pathname)
        cv2.imwrite(pathname, self.image)
        return pathname

    @classmethod
    def get_encoding(cls, image):
        rgb = image[:, :, ::-1]
        boxes = face_recognition.face_locations(rgb, model="hog")
        if not boxes:
            height, width, channels = image.shape
            top = int(height/3)
            bottom = int(top*2)
            left = int(width/3)
            right = int(left*2)
            box = (top, right, bottom, left)
        else:
            box = boxes[0]
        return face_recognition.face_encodings(image, [box])[0]

class Person():
    _last_id = 0

    def __init__(self, name=None):
        if name is None:
            Person._last_id += 1
            self.name = "person_%02d" % Person._last_id
        else:
            self.name = name
            if name.startswith("person_") and name[7:].isdigit():
                id = int(name[7:])
                if id > Person._last_id:
                    Person._last_id = id
        self.encoding = None
        self.faces = []

    def add_face(self, face):
        # add face
        self.faces.append(face)

    def calculate_average_encoding(self):
        if len(self.faces) == 0:
            self.encoding = None
        else:
            encodings = [face.encoding for face in self.faces]
            self.encoding = np.average(encodings, axis=0)

    def distance_statistics(self):
        encodings = [face.encoding for face in self.faces]
        distances = face_recognition.face_distance(encodings, self.encoding)
        return min(distances), np.mean(distances), max(distances)

    def save_faces(self, base_dir):
        pathname = os.path.join(base_dir, self.name)
        try:
            shutil.rmtree(pathname)
        except OSError as e:
            pass
        os.mkdir(pathname)
        for face in self.faces:
            face.save(pathname)

    def save_montages(self, base_dir):
        images = [face.image for face in self.faces]
        montages = imutils.build_montages(images, (128, 128), (6, 2))
        for i, montage in enumerate(montages):
            filename = "montage." + self.name + ("-%02d.png" % i)
            pathname = os.path.join(base_dir, filename)
            cv2.imwrite(pathname, montage)

    @classmethod
    def load(cls, pathname, face_encodings):
        basename = os.path.basename(pathname)
        person = Person(basename)
        for face_filename in os.listdir(pathname):
            face_pathname = os.path.join(pathname, face_filename)
            image = cv2.imread(face_pathname)
            if image.size == 0:
                continue
            if face_filename in face_encodings:
                face_encoding = face_encodings[face_filename]
            else:
                print(pathname, face_filename, "calculate encoding")
                face_encoding = Face.get_encoding(image)
            if face_encoding is None:
                print(pathname, face_filename, "drop face")
            else:
                face = Face(face_filename, image, face_encoding)
                person.faces.append(face)
        print(person.name, "has", len(person.faces), "faces")
        person.calculate_average_encoding()
        return person

if __name__ == '__main__' :
    cursor = db.cursor(pymysql.cursors.DictCursor)
    sql = 'select face_group_number, encoding, face_list from face_group where party_id = %s order by face_group_number'
    cursor.execute(sql, 1)
    face_group_list = cursor.fetchall()
    print(len(face_group_list))
    for fc, i in face_group_list:
        print(np.array(fc[1]))
