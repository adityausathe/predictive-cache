from utils import search_pattern


def derive_trends_dependent_dow(transactions, patterns):
    hour_of_day_trend = {}
    for uid, u_patterns in patterns.items():
        u_trans = transactions[uid]
        hour_of_day_hist = []

        for patt in u_patterns:
            patt_hour_of_day_hist = {}

            for t in u_trans:
                t_page_only = list(map(lambda t_i: t_i[0], t))
                if len(t_page_only) >= len(patt):
                    idx = search_pattern(patt, t_page_only)
                    if idx >= 0:
                        t_hour = t[idx][1].hour
                        hour_id = int(t_hour / 3) * 3
                        t_weekday = t[idx][1].weekday()
                        if t_weekday not in patt_hour_of_day_hist:
                            patt_hour_of_day_hist[t_weekday] = {}
                        if hour_id not in patt_hour_of_day_hist[t_weekday]:
                            patt_hour_of_day_hist[t_weekday][hour_id] = 0

                        patt_hour_of_day_hist[t_weekday][hour_id] += 1

            hour_of_day_hist.append((patt, patt_hour_of_day_hist))

        hour_of_day_trend[uid] = hour_of_day_hist

    for uid, trend in hour_of_day_trend.items():
        print("\n\n========= DOW->Hourly Trend =========", uid)
        print(trend)

    return hour_of_day_trend


def derive_trends_independent_dow(transactions, patterns):
    hourly_trend = {}
    dow_trend = {}
    consol_trend = {}
    for uid, u_patterns in patterns.items():
        u_trans = transactions[uid]
        hourly_hist = []
        dow_hist = []
        consol_hists = []
        for patt in u_patterns:
            patt_hourly_hist = {}
            patt_dow_hist = {}
            for t in u_trans:
                t_page_only = list(map(lambda t_i: t_i[0], t))
                if len(t_page_only) >= len(patt):

                    idx = search_pattern(patt, t_page_only)
                    if idx >= 0:
                        t_hour = t[idx][1].hour
                        hour_id = int(t_hour / 3) * 3
                        if hour_id not in patt_hourly_hist:
                            patt_hourly_hist[hour_id] = 0
                        patt_hourly_hist[hour_id] += 1

                        t_weekday = t[idx][1].weekday()
                        if t_weekday not in patt_dow_hist:
                            patt_dow_hist[t_weekday] = 0
                        patt_dow_hist[t_weekday] += 1

                        # print('++++++',idx,patt, t_hr)
            hourly_hist.append((patt, patt_hourly_hist))
            dow_hist.append((patt, patt_dow_hist))
            consol_hists.append((patt, patt_hourly_hist, patt_dow_hist))

            print(patt, patt_hourly_hist, patt_dow_hist)

        hourly_trend[uid] = hourly_hist
        dow_trend[uid] = dow_hist
        consol_trend[uid] = consol_hists

    """
    for uid, trend in hourly_trend.items():
        print("\n\n========= Hourly Trend =========",uid)
        print(trend)


    for uid, trend in dow_trend.items():
        print("\n\n========= DOW Trend =========",uid)
        print(trend)
    """
    return consol_trend
