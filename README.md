Spell Corrector Readme
======================
You can use this tool to generate noisy channel model parameters, test the performance and prune the model. Then you can use the model to do spell correction task.

+ Project name
```
spelling-corrector (This is a java project)
```
+  Project orgnization
```
project |-src |-lib |-bin |-out |-data |-build.xml
```

Build project
---------
In the project `SpellCorrectorBuild` root directory, use `ant` command to build the project, and ouput a jar file `SpellCorrectorBuild.jar`  in directory `./out/` .

How to use
---------
2. Prepare the dictionary file `words.txt` , original spell data file `final.out`  and parameter file `parameter` , and put`SpellCorrectorBuild.jar`  in the same directory.
3. Use command `java -jar SpellCorrectorBuild.jar` to run the project.
4. You can get a `test_result.txt`  file to save the test infomation, and `channle_data.txt` to save the noisy channel model parameter.

File descriptions
---------
- `channel_model.txt`
The file is like the format:
`(word_slice key_slice log_probability)`
- `parameter`
A json format file:
```
{

	"model_file": "chnnel_data.txt",
	"train_file": "final.out",
	"dic_file": "words.txt",
	"equal_prob": 0.9,
	"most_dis": 2,
	"context_num": 2,
	"transfer_freq": "loglog",
	"top_num": 3,
	"train": "yes",
	"test": "yes",
	"prune": "no"
}
```

> 1. `equal_prob` is the probability of `p(x|y)` where `x=y`, you can tune `equal_prob` by a validation method.
> 2. `most_dis` is the most edit distance in your application, usually you can set as 2.
> 3. `context_num` is the context window you use in the model, the larger context you use the more precise model you will get, but the model parameters will be increased.   Usually you can set as 2.
> 4. `transfer_freq` is the type of approache you use to smooth your frequency. You can set as `loglog`, `log` and `no`.  Using `loglog` soothing approache will get the highest precision. Using `no` means that you will use the original frequency.
> 5. `top_num` is the number of ranked candidates you will get. 
> 6. `train`, `test` and `prune` are the parameters that if you want to train the model, test the model or prune the model again. If you want, you can set it as `yes`, otherwise you can set it as `no`.
> 7. Another parameter `smooth value` is the value you will use when the score of a pair is not in your model due to the sparseness of the model. We calculate the value by the average of the 10 smallest data in `channle_data.txt` file.

Example data
------------
In the `data` directory, there is some example data. You can refer to it.
+ `final.out` is the original train file, it's of json format. A line represents a case of miss spelling. The format is as follows.

> 1. `word` is the word user wants to input.
> 2. `key` is the word user actually inputs.
> 3. `cnt` is the number of this case in the input method engine (IME) user data.
> 4. `match_type` is wether this input is precise or predict by the IME.
> 5. `cor_type` is wether this input is a "corrector" type or "spell check" type (we only consider spell check type, because IME can correct missing input by keyboard position), 
> 6. If `cor_type` is "spell check", then `spell_info` is the information of this spelling. It includes the follows. `spell_in` is the word user actually inputs. `spell_out` is the word user wants to input. `spell_type` is the missing input type, including `__ins__` (user inserts an additional letter), `__del__` (user deletes a letter), `__tra__` (user transposes two letters). `spell_pos` is the position of this missing input. `predict_type` is wether this input is precise or predict by the IME. `evidence_len` is the length of the word user wants to input.

+ `words.txt` is the vocabulary file.
+ `test_data.txt` is the test file, and each line is a missing input case. The first column is the word user wants to input, and the second column is the word user actually inputs.
+ `test_result.txt` is the test results of the model. `prune_data.txt` is the pruned model file. `channel_data.txt` and `channel_data_loglog.txt` are the output model file.

Presentation
-------------
For more details about the spell correction techniques, you can follow this tutorial [here](http://pangolulu.github.io/2015/10/27/spell-corrector/).
Reference
---------
+  The main method is based on Noisy Channel Model and an improved method from Microsoft Research http://ucrel.lancs.ac.uk/acl/P/P00/P00-1037.pdf

License
---------
spelling-corrector is published under MIT License

Copyright (c) 2015 Yu Gong (@pangolulu)
```
Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
