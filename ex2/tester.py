import filecmp

output_dir = "output/"
expected_output_dir = "expected_output/"

def simple_tester():
    # for the tester to work - both directories should contains files with identical names (!).

    dcmp = filecmp.dircmp(output_dir, expected_output_dir)

    if dcmp.left_only:
        print("WARNING: The following outputs have no expected-output analogue and therefore are not tested: ", dcmp.left_only)

    assert len(dcmp.diff_files) == 0, f"ERROR: The following outputs are wrong: {dcmp.diff_files}"
    
    print("SUCCESS: Tested the following files: ", dcmp.same_files)


if __name__ == '__main__':
    simple_tester()
