import cv2
import config
import numpy as np

orb = cv2.ORB_create()
bf_matcher = cv2.BFMatcher(cv2.NORM_HAMMING, crossCheck=True)

# Returns the a tuple of features: (keypoints, descriptors).
def featurize(self, image):
    return orb.detectAndCompute(image)

# Returns a list of "good" matches determines by a preset threshold.
def extract_good_matches(self, matches):
    good = []
    for i, (m, n) in enumerate(matches):
        if m.distance < (config.DISTANCE_THRESH * n.distance):
            good.append(m)
    return good

# Determines how close of a match two images are.
def match(self, test_data, train_data):
    test_kp, test_des = test_data
    train_kp, train_des = train_data

    score = 0

    # Find matches in keypoints using FLANN.
    matches = self.extract_good_matches(bf_matcher.match(test_des, train_des))

    # Calculate the threshold for detecting a match.
    test_threshold = 0.1 * len(test_kp)
    train_threshold = 0.1 * len(train_kp)
    threshold = max(test_threshold, train_threshold)

    if len(matches) < threshold:
        return None
    else:
        return len(matches)

def decode(raw_data, gray_scale = False):
    img_array = np.asarray(bytearray(raw_data), dtype=np.int8)
    if gray_scale:
        cv_image = cv2.imdecode(img_array, 0)
    else:
        cv_image = cv2.imdecode(img_array, -1)
    return cv_image

def encode(img, jpeg_quality = 95):
    result, data = cv2.imencode('.jpg', img, [cv2.IMWRITE_JPEG_QUALITY, jpeg_quality])
    raw_data = data.tostring()
    return raw_data
