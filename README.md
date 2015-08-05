Spell Corrector Readme
======================
You can use this tool to generate noisy channel model parameters, test the performance and prune the model. Then you can use the model to do spell correction task.

+ Project name
```
spelling-corrector (This is a java project)
```
+  Project orgnization
```
project |-src |-lib |-bin |-out |-build.xml
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
	"train": "no",
	"test": "no",
	"prune": "yes"
}
```

> 1. `equal_prob` is the probability of `p(x|y)` where `x=y`, you can tune `equal_prob` by a validation method.
> 2. `most_dis` is the most edit distance in your application, usually you can set as 2.
> 3. `context_num` is the context window you use in the model, the larger context you use the more precise model you will get, but the model parameters will be increased.   Usually you can set as 2.
> 4. `transfer_freq` is the type of approache you use to smooth your frequency. You can set as `loglog`, `log` and `no`.  Using `loglog` soothing approache will get the highest precision. Using `no` means that you will use the original frequency.
> 5. `top_num` is the number of ranked candidates you will get. 
> 6. `train`, `test` and `prune` are the parameters that if you want to train the model, test the model or prune the model again. If you want, you can set it as `yes`, otherwise you can set it as `no`.
> 7. Another parameter `smooth value` is the value you will use when the score of a pair is not in your model due to the sparseness of the model. We calculate the value by the average of the 10 smallest data in `channle_data.txt` file.


Reference
---------
+  The main method is based on Noisy Channel Model and an improved method from Microsoft Research http://ucrel.lancs.ac.uk/acl/P/P00/P00-1037.pdf
