import datetime
import logging
from collections import defaultdict

from gsp import GSP
from persistence_operations import fetch_access_records

logging.basicConfig(level=logging.DEBUG)


def create_transactions(min_record_threshold):
    records = fetch_access_records()
    users_data = defaultdict(lambda: list(range(0, 0)))
    for r in records:
        users_data[r[0]].append(r)

    trans_with_dt = {}
    transactions = {}
    for uid in sorted(users_data, reverse=True, key=lambda key: len(users_data[key])):
        if len(users_data[uid]) < min_record_threshold:
            continue
        print(uid, len(users_data[uid]))

        u_trans_with_dt = []
        u_trans = []
        p_tt = datetime.datetime(1970, 1, 1)
        c_tran = []
        c_tran_with_dt = []
        for _, timestamp, page in users_data[uid]:
            delta = timestamp - p_tt
            delta_hrs = delta.total_seconds() // 3600
            # print(delta_hrs, delta)
            if delta_hrs > 0 and delta.days < 999:
                u_trans.append(c_tran)
                u_trans_with_dt.append(c_tran_with_dt)
                c_tran = []
                c_tran_with_dt = []

            c_tran_with_dt.append((page, timestamp))
            c_tran.append(page)
            p_tt = timestamp

        transactions[uid] = u_trans
        trans_with_dt[uid] = u_trans_with_dt

    """
    for k,v in transactions.items():
        print('tr', k, len(v))

    for k,v in trans_with_dt.items():
        print('tr_w_dt', k, len(v))
    """
    return transactions, trans_with_dt


def extract_patterns(transactions, min_support_threshold):
    user_gsp_patterns = {}
    for uid, transList in transactions.items():
        gsp_patterns = GSP(transList).search(min_support_threshold)

        print("========= Status =========", uid)
        # print("Transactions: {}".format(transactions))
        print("GSP: {}".format(gsp_patterns))

        if len(gsp_patterns) > 1:
            len_wise_patterns = list(map(lambda d: list(d.keys()), gsp_patterns[1:]))
            all_len_patterns = [patt for len_patts in len_wise_patterns for patt in len_patts]
            print("Found patterns of length >=2 for user {} and patts: {}".format(uid, all_len_patterns))
            user_gsp_patterns[uid] = all_len_patterns

    print("Filtered GSP patterns:\n", user_gsp_patterns)
    return user_gsp_patterns
