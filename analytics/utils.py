import json


def dump_data(patterns):
    with open("patterns.json", "w") as outfile:
        json.dump(patterns, outfile)


def load_data():
    with open("patterns.json", "r") as infile:
        patterns = json.load(infile)

    return patterns


def search_pattern(pat, txt):
    M = len(pat)
    N = len(txt)

    lps = [0] * M
    j = 0

    _find_largest_prefixes(pat, M, lps)

    i = 0
    while i < N:
        if pat[j] == txt[i]:
            i += 1
            j += 1

        if j == M:
            return i - j

        elif i < N and pat[j] != txt[i]:
            if j != 0:
                j = lps[j - 1]
            else:
                i += 1
    return -1


def _find_largest_prefixes(pat, M, lps):
    _len = 0
    i = 1

    while i < M:
        if pat[i] == pat[_len]:
            _len += 1
            lps[i] = _len
            i += 1
        else:
            if _len != 0:
                _len = lps[_len - 1]
            else:
                lps[i] = 0
                i += 1
