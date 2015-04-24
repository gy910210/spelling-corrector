__author__ = 'Gongyu'

import edit_distance


def dump_error_data(train_file, error_file):
    infile = open(train_file, 'r')
    outfile = open(error_file, 'w')

    for line in infile:
        strs = line.strip().split('\t')
        if strs[0] == strs[1]:
            continue
        else:
            dis, pos_vector = edit_distance.edit(strs[0], strs[1])

