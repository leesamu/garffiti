from flask import Flask, render_template, request, url_for, jsonify
from base64 import b64encode, b64decode

app = Flask(__name__)

img = ""

@app.route('/upload', methods=["POST", "GET"])
def up():
    global img
    img = request.get_json()
    return img["image"]

def main():
    app.run()

if __name__ == "__main__":
    main()
