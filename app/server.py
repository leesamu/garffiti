from flask import Flask, render_template, request, url_for, jsonify
from collections import deque, defaultdict
from argparse import ArgumentParser
from base64 import b64encode, b64decode

app = Flask(__name__)
default_port = 8888

loc_to_image_map = defaultdict(list)

@app.route('/data', methods=['POST'])
def home():
    data = request.get_json()
    
    print("\n\n\n")
    print(data)
    print("\n\n\n")

    #if data is None:
    #    return

    gps_loc = ",".join([ str(x) for x in data["gps_loc"] ])
    image = b64decode(data["image"])
    annot = b64decode(data["annotation"])

    loc_to_image_map[gps_loc].append(image)

    print(gps_loc)
    print(loc_to_image_map)
    print(annot)

    return "It works"

def arguments():
    parser = ArgumentParser()
    parser.set_defaults(show_path=False, show_similarity=False)

    parser.add_argument("-p", "--show_path", action="store_true", dest="show_path")
    parser.add_argument("-s", "--show_sim", action="store_true", dest="show_similarity")

    return parser.parse_args()


def main():
    args = arguments()

    SHOW_PATH = args.show_path
    SHOW_SIMILARITY = args.show_similarity
 
    #app.run(debug=True, host='0.0.0.0')
    app.run()

if __name__ == "__main__":
    main()
