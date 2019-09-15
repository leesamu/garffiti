# Distance threshold for the ratio test
DISTANCE_THRESH = 0.75

# FLANN parameters
FLANN_INDEX_KDTREE = 1
INDEX_PARAMS = dict(algorithm = FLANN_INDEX_KDTREE, trees = 5)
SEARCH_PARAMS = dict(checks = 50)
