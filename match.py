import cv2
import config

class Matcher:
    """
    Creates an image matcher object that can determine
    whether two images match using SURF features.
    """
    def __init__(self):
        self.flann = cv2.FlannBasedMatcher(config.INDEX_PARAMS, config.SEARCH_PARAMS)

    def find_matches(self, matches):
        good = []
        for i, (m, n) in enumerate(matches):
            if m.distance < (config.DISTANCE_THRESH * n.distance):
                good.append(m)
        return good

    """
    Returns the number of good matches if it is over
    the cutoff threshold, otherwise returns None.
    """
    def match(self, test_data, train_data):
        test_kp, test_des = test_data
        train_kp, train_des = train_data

        score = 0

        # Find matches in keypoints using FLANN.
        matches = self.find_matches(self.flann.knnMatch(test_des, train_des, k = 2))

        # Calculate the threshold for detecting a match.
        test_threshold = 0.1 * len(test_kp)
        train_threshold = 0.1 * len(train_kp)
        threshold = max(test_threshold, train_threshold)

        if len(matches) < threshold:
            return None
        else:
            return len(matches)

