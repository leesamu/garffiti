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
    annot = decode(data["annotation"])

    image_map[gps_loc]["raw"].append(image)
    image_map[gps_loc]["featurized"].append(featurize(image))
    image_map[gps_loc]["annotation"].append(annot)

# Explore mode for viewing current art
@app.route('/explore', methods=['POST'])
def read():
    data = request.get_json()

    gps_loc = ",".join([ str(x) for x in data["gps_loc"] ])
    image = decode(data["image"])

    ft = featurize(image)

    closest_match_score = 0
    best_match = None
    for ft_img in image_map[gps_loc]["featurized"]:
        score = match(ft, ft_img)
        if score is None:
            continue
        if score > closest_match_score or closest_match_score == 0:
            best_match = ft_img

    # return transform(best_image, best_annotation, best_features, query_image, query_features)
    
    return encode(image)

@app.route('/gps', methods=['GET']):
def gps():
    return json.dumps({ "gps": list(image_map.keys()) })

def arguments():
    parser = ArgumentParser()
    parser.set_defaults(dummy=False)

    parser.add_argument("-t", "--tolerance", action="store", type=int, dest="tolerance")

    return parser.parse_args()


def main():

    args = arguments()
    TOLERANCE = args.tolerance

    #app.run(debug=True, host='0.0.0.0')
    app.run()

if __name__ == "__main__":
    main()
