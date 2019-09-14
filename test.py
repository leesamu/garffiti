import cv2
from match import Matcher

if __name__ == "__main__":
    test_1 = cv2.imread("test_images/IMG_0035.jpg", 0)
    test_2 = cv2.imread("test_images/IMG_0038.jpg", 0)
    matcher = Matcher()
    surf = cv2.SURF()
    test_1_data = surf.detectAndCompute(test_1, None)
    test_2_data = surf.detectAndCompute(test_2, None)
    num_matches = matcher.match(test_1_data, test_2_data)
    print num_matches
