CFLAGS=-Isrc

compile::
	mono ~/.bin/SugarCpp/SugarCpp.CommandLine.exe src_sugar/*
	# mono ~/.bin/SugarCpp/SugarCpp.CommandLine.exe src_sugar/*/*
	rsync --remove-source-files -av --filter="+ */" --filter="-! *.cpp*" src_sugar/ src
	rsync --remove-source-files -av --filter="+ */" --filter="-! *.h*" src_sugar/ src

test::
	mono ~/.bin/SugarCpp/SugarCpp.CommandLine.exe test_sugar/*
	rsync --remove-source-files -av --filter="+ */" --filter="-! *.cpp*" test_sugar/ src
	g++ $(CFLAGS) test/test*.cpp test/petitsuite.cpp -std=c++11 -g -o test.out
	./test.out
