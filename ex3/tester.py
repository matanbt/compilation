import subprocess
import os

# ---- CONFIG ----
input_dir = "./input/"
output_dir = "./output/"
expected_output_dir = "./expected_output/"
logs_dir = "./logs/"
flag_save_logs = True

test_files = [
    "TEST_01_Print_Primes.txt"
     ]

def list_test_files_by_dir():
    global test_files
    test_files = os.listdir(input_dir)


def run_on_test_files():
    # assumes all files in 'input_files' located in path 'input/'
    for i, filename in enumerate(test_files):
        input_file =  input_dir + filename                        # input/test.txt
        output_file = output_dir + filename[:-4] + "_Output.txt"  # output/test_Output.txt
        print("---- #" + str(i) + " / " + str(len(test_files)) + " | Running: " + filename + " ----")
        result = subprocess.run(['java', '-jar', 'COMPILER', input_file, output_file],
                                capture_output=True, text=True)
        save_log(filename, result.stdout)


def simple_tester():
    # compares outputs to expected ones, IGNORING starting/ending whitespaces
    print("\n\n=============== SIMPLE_TESTER ================")

    for i, filename in enumerate(test_files):
        print("---- #" + str(i) + " / " + str(len(test_files)) + " | Checking: " + filename + " ----")
        input_file =  input_dir + filename                                             # input/test.txt
        output_file = output_dir + filename[:-4] + "_Output.txt"                       # output/test_Output.txt
        expected_file = expected_output_dir + filename[:-4] + "_Expected_Output.txt"   # expected_output/test_Expected_Output.txt
        with open(output_file, 'r') as f1, open(expected_file, 'r') as f2:
            s1 = f1.read().strip()
            s2 = f2.read().strip()
            assert s1 == s2, "\n---> Test failed on: " + filename +\
                             "\n---> Got: " + s1 + "   Expected: " + s2 +\
                             "\n---> Please check : " + logs_dir + filename[:-4] + "_log_run.txt" +\
                             "\n---> Or rerun : java -jar COMPILER " + filename + " " + output_file

    print("\n============= SUCCESS - SIMPLE_TESTER ==============")


# ------- Logs: ---------
def init_logs_dir():
    subprocess.run(['rm', '-r', logs_dir])
    subprocess.run(['mkdir', logs_dir])


def save_log(_fname, _stdout):
    if flag_save_logs:
        log_file = logs_dir + _fname[:-4] + "_log_run.txt"
        with open(log_file, "w") as f:
            f.write(str(_stdout))




if __name__ == '__main__':
    # init dir to save logs
    init_logs_dir()

    # lists test files located in input_dir
    list_test_files_by_dir()

    # runs the compiler on all files
    run_on_test_files()

    # validate the result from last step
    simple_tester()
