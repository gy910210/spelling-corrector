Spell Corrector Readme
======================
You can use this tool to generate noisy channel model parameters and test the performance. Then you can use the model to do spell correction task.

+ ## Project name
SpellCorrectorBuild (This is a java project)

+ ## Project orgnization
project |-src |-lib |-bin |-out |-build.xml

+ ## Build project
In the project "SpellCorrectorBuild" root directory, use Ant command to build the project, and ouput a jar file "SpellCorrectorBuild.jar" in directory "/out/".

+ ## How to use
2. Prepare the dictionary file "words.txt", original spell data file "final.out" and parameter file "parameter", and put "SpellCorrectorBuild.jar" in the same directory.
3. Create a "/tmp/" directory to save the middle file: "train_data.txt", "corpus_data.txt", "error_data.txt", "count_data.txt".
4. Use command "java -jar SpellCorrectorBuild.jar" to run the project.
5. You can get a "test_result.txt" file to save the test infomation, and "channle_data.txt" to save the noisy channel model parameter.

+ ## File descriptions
- ### "parameter"
A json format file:
{
    "tmp_dir": "tmp/",
	"channel_file": "channel_data.txt",
	"input_file": "final.out",
	"words_file": "words.txt",
	"test_file": "tmp/train_data.txt",
	"equal_prob": 1.0,
	"smooth_prob": 0.00000005,
	"most_dis": 2,
	"context_num": 2,
	"top_num": 3
}

- ### "channle_data.txt"
The file is like the format:
(word_slice \t key_slice \t log_probability)

+ ## Tips
+ If you don't change the original spell data, you can reuse the middle file in tmp directory so that the train process can be fast.
+ The noisy channel model parameters file "channel_data.txt" can be resued all the time.

+ ## Reference
The main method is based on Noisy Channel Model and an improved method from Microsoft Research
http://ucrel.lancs.ac.uk/acl/P/P00/P00-1037.pdf

