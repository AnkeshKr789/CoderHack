"""Test configuration and constants"""

# API Configuration
BASE_URL = "http://localhost:8081/coderhack/api/v1"
USER_ID = "test123"
TIMEOUT = 1

# Test Data
TEST_USER = {
    "userId": USER_ID,
    "username": "test"
}

# Badge Score Thresholds
BADGE_SCORES = {
    "CODE_NINJA": 1,
    "CODE_CHAMP": 30,
    "CODE_MASTER": 60
} 