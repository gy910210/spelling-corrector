__author__ = 'Gongyu'

import edit_distance


def split_error(word, key, context_num=2):
    re_list = []
    dis, pos_vector = edit_distance.edit(word, key)

    for pos in xrange(len(pos_vector)):
        if pos_vector[pos][0] != pos_vector[pos][1]:
            re_list.append(pos_vector[pos][0] + '|' + pos_vector[pos][1])

            for slide in xrange(1, context_num + 1):
                head = max(0, pos - slide)
                for i in xrange(head, pos + 1):
                    if i + slide >= len(pos_vector):
                        continue
                    slice_w = ''
                    slice_s = ''
                    for ii in xrange(slide + 1):
                        if pos_vector[i + ii][0] != '#':
                            slice_w += pos_vector[i + ii][0]
                        if pos_vector[i + ii][1] != '#':
                            slice_s += pos_vector[i + ii][1]
                    re_list.append(slice_w + '|' + slice_s)
    return re_list


def dump_error_data(train_file, error_file, context_num=2):
    infile = open(train_file, 'r')
    outfile = open(error_file, 'w')
    error_map = {}

    print 'dump error data...'
    # error_list = error_map.items()
    # error_list.sort(key=lambda item: item[1])
    for key in error_map.keys():
        outfile.write(key + '\t' + str(error_map[key]) + '\n')
        outfile.flush()

if __name__ == '__main__':
    dump_error_data('training_data.txt', 'error_data.txt')
    # print split_error('you', 'yiu')