__author__ = 'Gongyu'

import edit_distance


def split_error(word, key, context_num=2):
    re_list = []
    dis, pos_vector = edit_distance.edit(word, key)

    for pos in xrange(len(pos_vector)):
        if pos_vector[pos][0] != pos_vector[pos][1]:
            re_list.append(pos_vector[pos][0] + '|' + pos_vector[pos][1])
            # print pos_vector[pos][0] + '\t' + pos_vector[pos][1]

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
                    # print slice_w + '\t' + slice_s
    return re_list


def dump_error_data(train_file, error_file, context_num=2):
    infile = open(train_file, 'r')
    outfile = open(error_file, 'w')
    # error_map = {}

    print 'read train data and split...'
    for line in infile:
        strings = line.strip().split('\t')
        if strings[0] == strings[1]:
            continue
        else:
            # print strings[0], strings[1]
            split_list = split_error(strings[0], strings[1], context_num)
            for pair in split_list:
                outfile.write(pair + '\t' + str(strings[2]) + '\n')
                outfile.flush()
    infile.close()
    outfile.close()

    '''
    print 'dump error data...'
    # error_list = error_map.items()
    # error_list.sort(key=lambda item: item[1])
    for key in error_map.keys():
        outfile.write(key + '\t' + str(error_map[key]) + '\n')
        outfile.flush()
    '''


'''
def dump_error_data_split(train_file, error_file, split_file_num=100, context_num=2):
    line_num = 0
    infile = open(train_file, 'r')
    for line in infile:
        line_num += 1
    infile.close()
    print 'line num: ' + str(line_num)

    line_num_each_file = int(line_num / split_file_num)
    for i in xrange(split_file_num):
        # print 'split file num: ' + str(i)
        print str(i*line_num_each_file) + ' ~ ' + str((i+1)*line_num_each_file)
        if i != split_file_num - 1:
            dump_error_data(train_file, error_file+'_'+str(i)+'.txt', context_num,
                            i*line_num_each_file, (i+1)*line_num_each_file)
        else:
            dump_error_data(train_file, error_file+'_'+str(i)+'.txt', context_num,
                            i*line_num_each_file, line_num)
        gc.collect()
        time.sleep(5)
'''

if __name__ == '__main__':
    # dump_error_data_split('training_data.txt', 'error\\error_data')
    dump_error_data('training_data.txt', 'error_data.txt')
    # print split_error('you', 'yiu')