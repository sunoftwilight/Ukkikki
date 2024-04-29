import face_recognition
import numpy as np
from datetime import datetime
import cv2
import os
import shutil
import imutils

ratio = 1.0
similarity_threshold = 0.7

def compare_with_known_persons(self, face, persons):
    if len(persons) == 0:
        return None

    # see if the face is a match for the faces of known person
    encodings = [person.encoding for person in persons]
    distances = face_recognition.face_distance(encodings, face.encoding)
    index = np.argmin(distances)
    min_value = distances[index]
    if min_value < self.similarity_threshold:
        # face of known person
        persons[index].add_face(face)
        # re-calculate encoding
        persons[index].calculate_average_encoding()
        face.name = persons[index].name
        return persons[index]

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

# def detect_faces(frame):
#     rgb = frame[:, :, ::-1]    # 이미지를 RGB format으로 변환
#     print("rgb")
#     print(rgb)
#     # 얼굴 영역을 알아낸다
#     boxes = face_recognition.face_locations(rgb, model="hog")
#     print("boxes")
#     print(boxes)
#     if not boxes:
#         return []
#
#     # 얼굴 영역을 찾았음. face_encoding을 계산
#     encodings = face_recognition.face_encodings(rgb, boxes)
#     print("encodings")
#     print(encodings)
#     # Face 생성하여 리턴
#     faces = []
#
#     now = datetime.now()
#     str_ms = now.strftime('%Y%m%d_%H%M%S.%f')[:-3] + '-'
#
#     for i, box in enumerate(boxes):
#         face_image = get_face_image(frame, box)   # 얼굴 이미지 추출
#         face = Face(str_ms + str(i) + ".png", face_image, encodings[i])
#         faces.append(face)
#     return faces

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
    frame = cv2.imread('C:\\test.jpg')
    faces = detect_faces(frame)
    image = face_recognition.load_image_file('C:\\test.jpg')
    face_locations = face_recognition.face_locations(image)
    # for face in face_locations:

    print(face_locations)
    for face in faces :
        face.save("C:\\")
