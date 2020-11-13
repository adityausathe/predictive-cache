import sys

from pattern_mining import create_transactions, extract_patterns
from persistence_operations import save_trends_dependent_dow, save_trends_independent_dow
from trend_derivation import derive_trends_dependent_dow, derive_trends_independent_dow
from utils import dump_data, load_data

# (useful for debugging)
use_stale_patterns = False


def main(min_record_threshold, min_support_threshold, derive_trends, save_trends):
    transactions, trans_with_dt = create_transactions(min_record_threshold)

    if not use_stale_patterns:
        # create new patterns
        patterns = extract_patterns(transactions, min_support_threshold)
        # save patterns (useful for debugging)
        dump_data(patterns)
    else:
        # load preexisting patterns (useful for debugging)
        patterns = load_data()

    trends = derive_trends(trans_with_dt, patterns)

    save_trends(trends)


# $ python main.py Dependent 100 0.5
if __name__ == '__main__':
    trend_type = sys.argv[1]
    count_threshold = int(sys.argv[2])
    support_threshold = float(sys.argv[3])
    if trend_type == 'Dependent':
        main(count_threshold, support_threshold, derive_trends_dependent_dow, save_trends_dependent_dow)
    elif trend_type == 'Independent':
        main(count_threshold, support_threshold, derive_trends_independent_dow, save_trends_independent_dow)
    else:
        RuntimeError("Unrecognized Trend Type")
