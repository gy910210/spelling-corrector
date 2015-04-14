__author__ = 'Gongyu'


def edit(string1, string2):
    m = len(string1)
    n = len(string2)
    record = [[0] * (n + 1) for i in xrange(m + 1)]

    for ii in xrange(n + 1):
        record[0][ii] = ii

    for jj in xrange(m + 1):
        record[jj][0] = jj

    for i in xrange(1, m + 1):
        for j in xrange(1, n + 1):
            tmp = 0
            if string1[i - 1] != string2[j - 1]:
                tmp = 1
            record[i][j] = min(min(record[i - 1][j] + 1, record[i][j - 1] + 1), record[i - 1][j - 1] + tmp)
    return record[m][n]


def edit_dl(string1, string2):
    m = len(string1)
    n = len(string2)
    record = [[0] * (n + 1) for i in xrange(m + 1)]

    for ii in xrange(n + 1):
        record[0][ii] = ii

    for jj in xrange(m + 1):
        record[jj][0] = jj

    for i in xrange(1, m + 1):
        for j in xrange(1, n + 1):
            tmp = 0
            if string1[i - 1] != string2[j - 1]:
                if i == 1 or j == 1:
                    tmp = 1
                elif string1[i - 1] != string2[j - 2] or string1[i - 2] != string2[j - 1]:
                    tmp = 1
            record[i][j] = min(min(record[i - 1][j] + 1, record[i][j - 1] + 1), record[i - 1][j - 1] + tmp)
    return record[m][n]

if __name__ == '__main__':
    print edit_dl('bacd', 'cbd')
