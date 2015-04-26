__author__ = 'Gongyu'


def edit(string1, string2):
    m = len(string1)
    n = len(string2)

    record = [[0] * (n + 1) for i in xrange(m + 1)]
    pos_record = [[()] * (n + 1) for i in xrange(m + 1)]

    for ii in xrange(1, n + 1):
        record[0][ii] = ii
        pos_record[0][ii] = (0, ii - 1, '#', string2[ii - 1])

    for jj in xrange(1, m + 1):
        record[jj][0] = jj
        pos_record[jj][0] = (jj - 1, 0, string1[jj - 1], '#')

    for i in xrange(1, m + 1):
        for j in xrange(1, n + 1):
            tmp = 0
            if string1[i - 1] != string2[j - 1]:
                tmp = 1

            pos = ()
            if record[i - 1][j] + 1 < record[i][j - 1] + 1:
                if record[i - 1][j] + 1 < record[i - 1][j - 1] + tmp:
                    pos = (i - 1, j, string1[i - 1], '#')
                else:
                    pos = (i - 1, j - 1, string1[i - 1], string2[j - 1])
            else:
                if record[i][j - 1] + 1 < record[i - 1][j - 1] + tmp:
                    pos = (i, j - 1, '#', string2[j - 1])
                else:
                    pos = (i - 1, j - 1, string1[i - 1], string2[j - 1])
            pos_record[i][j] = pos
            record[i][j] = min(record[i - 1][j] + 1, record[i][j - 1] + 1, record[i - 1][j - 1] + tmp)

    pos_vector = []
    i, j = m, n
    while i > 0 or j > 0:
        print i, j
        i, j, ch1, ch2 = pos_record[i][j]
        print ch1, ch2
        pos_vector.append((ch1, ch2))
    pos_vector.reverse()

    return record[m][n], pos_vector

'''
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
'''

if __name__ == '__main__':
    print edit('you', 'bu')
