__author__ = 'Gongyu'
import json


def dump_training_data(origin_file, format_file):
    infile = open(origin_file, 'r')
    outfile = open(format_file, 'w')

    cnt = 1
    for line in infile:
        if len(line.strip()) == 0:
            continue

        if cnt % 1000 == 0:
            print 'cnt: ' + str(cnt)

        data = json.loads(line.strip())
        if (data['cor_type'] == 'cor') or (data['cor_type'] == 'spe_cor'):
            continue
        
        if data['match_type'] == 'precise':
            outfile.write(data['word'] + '\t' + data['key'] + '\t' + str(data['cnt']) + '\n')
        elif data['match_type'] == 'predict':
            if data['cor_type'] == '':
                outfile.write(data['word'] + '\t' + data['word'] + '\t' + str(data['cnt']) + '\n')
            elif data['cor_type'] == 'spe':
                error_word = data['spell_info']['spell_in']
                for i in xrange(len(data['word'])):
                    if i < len(data['spell_info']['spell_out']):
                        continue
                    else:
                        error_word = error_word + data['word'][i]
                outfile.write(data['word'] + '\t' + error_word + '\t' + str(data['cnt']) + '\n')
        outfile.flush()
        cnt += 1

    infile.close()
    outfile.close()


if __name__ == '__main__':
    dump_training_data('final.out', 'training_data.txt')
