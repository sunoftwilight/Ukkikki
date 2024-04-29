from flask import Flask, render_template, request
import face_classifier
import numpy as np
import cv2
from PIL import Image
import io


app = Flask(__name__)
fc = face_classifier

@app.route('/upload')
def hello_world():
    return render_template('upload.html')

@app.route('/uploader', methods=['POST'])
def uploader_file():
    if request.method == 'POST':
        print(request)
        print(request.form)
        print(request.files)
        # 비디오 아웃풋명 및 디렉토리 네이밍을 위한 파라미터
        pk = request.form['PK']
        file = request.files.get("file")
        in_memory_file = io.BytesIO()
        file.save(in_memory_file)
        data = np.fromstring(in_memory_file.getvalue(), dtype=np.uint8)
        color_image_flag = 1  # cv2.IMREAD_COLOR로도 설정 가능
        image = cv2.imdecode(data, color_image_flag)

        faces = face_classifier.detect_faces(image)

        for f in faces :
            path = f.save("C:\\")
            img = cv2.imread(path)
            print(fc.Face.get_encoding(img))
    return 'upload'


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001)