import flask
from flask import Flask, render_template, request, url_for, jsonify
from collections import deque, defaultdict
from argparse import ArgumentParser
from base64 import b64encode, b64decode

import json
from cv import *

app = Flask(__name__)
default_port = 8888

# Take the location as a key and return the associated image, annotation, and feature vector
image_map = defaultdict(list)


# Annotation mode
@app.route('/save', methods=['POST'])
def save():
    data = request.get_json()

    gps_loc = ",".join([ str(x) for x in data["gps_loc"] ])
    image = decode(data["image"])
    annot = data["annotation"]

    image_map[gps_loc]["raw"].append(image)
    image_map[gps_loc]["featurized"].append(featurize(image))
    image_map[gps_loc]["annotation"].append(annot)

# Explore mode for viewing current art
@app.route('/explore', methods=['POST', 'GET'])
def read():
    data = request.get_json()

    gps_loc = ",".join([ str(x) for x in data["gps_loc"] ])
    image = decode(data["image"])

    img, kp, desc = featurize(image)
    ft = (kp, desc)

    closest_match_score = 0
    best_match = None
    for idx, ft_img in enumerate(image_map[gps_loc]["featurized"]):
        score = match(ft, ft_img)
        if score is None:
            continue
        if score > closest_match_score:
            best_match = image_map[gps_loc]["raw"][idx]

    # return transform(best_image, best_annotation, best_features, query_image, query_features)
    
    if best_match is None:
        print("We found nothing")
        return encode(cv2.cvtColor(image, cv2.COLOR_BGR2GRAY))
    print("We found a match")
    return encode(best_match)


@app.route('/gps', methods=['GET', 'POST'])
def gps():
    if flask.request.method == 'GET':
        points = { "gps" : image_map.keys() }
        for i in range(len(points["gps"])):
            points["gps"][i] = (float(points["gps"][i].split(",")[0]), float(points["gps"][i].split(",")[1]))
        return json.dumps(points)
    else:
        req = json.loads(flask.request.form.to_dict().keys()[0])
        print req
        gps_str = ",".join([str(req["lat"]), str(req["lng"])])
        image_map[gps_str] = None
        return "GPS point added!"

def arguments():
    parser = ArgumentParser()
    parser.set_defaults(dummy=False)

    #parser.add_argument("-t", "--tolerance", action="store", type=int, dest="tolerance")

    return parser.parse_args()


def main():
    args = arguments()
    #TOLERANCE = args.tolerance

    app.run(debug=True, host='0.0.0.0')
    #app.run()

if __name__ == "__main__":
    main()
