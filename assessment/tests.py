#!/usr/bin/env python

from test_utils import APIClient, TestAssertions
from test_config import USER_ID, TEST_USER, BADGE_SCORES
from test_runner import TestRunner

client = APIClient()
assertions = TestAssertions()

def create_new_user():
    """Create a new user with default values"""
    response = client.post("users", json=TEST_USER)
    assertions.assert_status_code(response, 200)
    assertions.assert_json_field(response, "userId", USER_ID)
    assertions.assert_json_field(response, "score", 0)
    assertions.assert_json_field(response, "badges", [])

def get_user_by_id():
    """Retrieve user details by ID"""
    response = client.get("users/{0}".format(USER_ID))
    assertions.assert_status_code(response, 200)
    assertions.assert_json_field(response, "userId", USER_ID)

def update_user_score():
    """Update user score and verify badge award"""
    response = client.put("users/{0}".format(USER_ID), json={"score": 10})
    assertions.assert_status_code(response, 200)
    assertions.assert_json_field(response, "score", 10)
    assertions.assert_json_field(response, "badges", ["CODE_NINJA"])

def award_badges_at_thresholds():
    """Verify badges are awarded at correct score thresholds"""
    for badge, score in BADGE_SCORES.items():
        response = client.put("users/{0}".format(USER_ID), json={"score": score})
        assertions.assert_status_code(response, 200)
        assert badge in response.json()["badges"], \
            "Badge {0} not awarded at score {1}".format(badge, score)

def enforce_badge_limit():
    """Verify maximum badge limit is enforced"""
    response = client.put("users/{0}".format(USER_ID), json={"score": 100})
    assertions.assert_status_code(response, 200)
    assert len(response.json()["badges"]) <= 3, "User has more than 3 badges"

def get_users_sorted_by_score():
    """Verify users list is sorted by score"""
    response = client.get("users")
    assertions.assert_status_code(response, 200)
    assertions.assert_sorted_by_field(response, "score")

def delete_user():
    """Delete user and verify response"""
    response = client.delete("users/{0}".format(USER_ID))
    assertions.assert_status_code(response, 204)

# Test cases
tests = {
    "Create New User": create_new_user,
    "Get User By ID": get_user_by_id,
    "Update User Score": update_user_score,
    "Award Badges At Thresholds": award_badges_at_thresholds,
    "Enforce Badge Limit": enforce_badge_limit,
    "Get Users Sorted By Score": get_users_sorted_by_score,
    "Delete User": delete_user
}

if __name__ == "__main__":
    TestRunner().run(tests) 