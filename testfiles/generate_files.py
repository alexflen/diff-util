from random import *

def gen_test03():
	file_in1 = open("03_file1.txt", "w")
	file_in2 = open("03_file2.txt", "w")
	file_answ = open("03_answ.txt", "w")

	seq1 = ["abcde" * 1000, "qwerty" * 954, "lolol" * 1002, "meowcat" * 902]
	seq2 = ["edcba" * 1000, "ytrewq" * 954, "wowow" * 1002, "tacwoem" * 902]
	seqcommon = ["commonline1" * 500, "comm2" * 790, "lineline4" * 579, "opopop" * 900]
	
	common = []
	for i in range(randint(400, 500)):
		com = choice(seqcommon)
		file_in1.write(com + "\n")
		file_in2.write(com + "\n")
		file_answ.write("|  " + com + "\n")
		for j in range(randint(0, 5)):
			file_in1.write(choice(seq1) + "\n")
		for j in range(randint(0, 5)):
			file_in2.write(choice(seq2) + "\n")

	file_in1.close()
	file_in2.close()
	file_answ.close()


def gen_test04():
	file_in1 = open("04_file1.txt", "w")
	file_in2 = open("04_file2.txt", "w")
	file_answ = open("04_answ.txt", "w")

	seq1 = ["abcde" * 4000, "qwerty" * 3954, "lolol" * 4002, "meowcat" * 3902]
	seq2 = ["edcba" * 4000, "ytrewq" * 3954, "wowow" * 4002, "tacwoem" * 3902]
	seqcommon = ["commonline1" * 2500, "comm2" * 2790, "lineline4" * 2579, "opopop" * 2900]
	
	common = []
	for i in range(randint(700, 800)):
		com = choice(seqcommon)
		file_in1.write(com + "\n")
		file_in2.write(com + "\n")
		file_answ.write("|  " + com + "\n")
		for j in range(randint(0, 5)):
			genned = choice(seq1) + "\n"
			file_in1.write(genned)
			file_answ.write("-  " + genned)
		for j in range(randint(0, 5)):
			genned = choice(seq2) + "\n"
			file_in2.write(genned)

	file_in1.close()
	file_in2.close()
	file_answ.close()	

seed(0)
gen_test03()
gen_test04()
