import datetime

import pymongo

myclient = pymongo.MongoClient("mongodb://localhost:27017/")
mydb = myclient["pred-cache-frame-db"]
user_trend_collection = mydb["userTrend"]
access_record_collection = mydb["accessRecord"]


def fetch_access_records():
    records = []
    for r in access_record_collection.find():
        records.append((r['userId'], datetime.datetime.strptime(r['accessTime'], '%Y-%m-%d %H:%M:%S'), r['resourceId']))
    return records


class UserTrend:
    def __init__(self, user_id, pattern_trends):
        self.user_id = user_id
        self.pattern_trends = pattern_trends

    def serialize(self):
        return {
            'userId': self.user_id,
            'patternTrends': [p.serialize() for p in self.pattern_trends]
        }


def save_trends_independent_dow(trends):
    class PatternTrend:
        def __init__(self, sequence, hourly_trend, dow_trend):
            self.sequence = sequence
            self.hourly_trend = hourly_trend
            self.dow_trend = dow_trend

        def serialize(self):
            return {
                'sequence': self.sequence,
                'trend': self.serialize_trends()
            }

        def serialize_trends(self):
            return {
                'hourlyTrend': {"hourRange_" + str(k): v for k, v in self.hourly_trend.items()},
                'dowTrend': {"dow_" + str(k): v for k, v in self.dow_trend.items()}
            }

    save_trends(trends, lambda u_trend: PatternTrend(u_trend[0], u_trend[1], u_trend[2]))


def save_trends_dependent_dow(trends):
    class PatternTrend:
        def __init__(self, sequence, dow_trend):
            self.sequence = sequence
            self.dow_trend = dow_trend

        def serialize(self):
            return {
                'sequence': self.sequence,
                'trend': {"dow_" + str(k): self.serialize_hourly(v) for k, v in self.dow_trend.items()}
            }

        def serialize_hourly(self, hourly):
            return {"hourRange_" + str(k): v for k, v in hourly.items()}

    save_trends(trends, lambda u_trend: PatternTrend(u_trend[0], u_trend[1]))


def save_trends(trends, pattern_trend):
    user_trend_collection.delete_many({})
    for uid, u_trends in trends.items():
        pattern_trend_entities = []
        for u_trend in u_trends:
            pattern_trend_entities.append(pattern_trend(u_trend))

        user_trend_entity = UserTrend(uid, pattern_trend_entities)

        user_trend_collection.insert_one(user_trend_entity.serialize())
