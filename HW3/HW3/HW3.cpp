﻿#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <stack>
#include <malloc.h>

using namespace std;

vector<string> input;
stack<string> st;
vector<int> error_msg;

typedef struct TreeNode {
	string node;
	TreeNode* LNode;
	TreeNode* RNode;
}TreeNode;


string error_message[5] = { "- 기호가 여러개 존재합니다.",
							"정수로 입력하여 주세요",
							"undefined" ,
							"(의 위치가 잘못되었습니다.",
							")의 위치가 잘못되었습니다." };

bool isInt(string str) {
	char value;
	bool ret = true;
	for (int i = 0; i < str.length(); i++) {
		value = str[i];
		if (value >= '0' && value <= '9' || value == '-')
			ret = true;
		else {
			ret = false;
		}
	}
	return ret;
}

void error_check_num(string check) {
	for (int i = 0; i < check.length(); i++) {
		//숫자 에러
		if (check[i] < '0' || check[i] > '9') {
			if (i == 0 && check[i] == '-') {
				continue;
			}
			else if (check[i] == '-' && i > 0) {
				error_msg.push_back(0);
			}
			error_msg.push_back(1);
		}
	}
}

void error_check_str(string check) {
	if (check.compare("MINUS") != 0 && check.compare("IF") != 0) {
		error_msg.push_back(2);
	}
}

string calculate(string op, string a, string b) {

	int a1 = stoi(a);
	int b1 = stoi(b);

	if (op[0] == 'M') {
		return to_string((a1 - b1));
	}
	else if (op[0] == 'I') {
		if (a1) {
			return to_string(b1);
		}
		else {
			return to_string(0);
		}
	}
}

void postorder(TreeNode* ptr)
{  //후위 트리 순회
	if (ptr) {

		postorder(ptr->LNode);
		postorder(ptr->RNode);
		st.push(ptr->node);
		if (ptr->node == "MINUS" || ptr->node == "IF") {
			string op = st.top();
			st.pop();
			string b = st.top();
			st.pop();
			string a = st.top();
			st.pop();
			string res = calculate(op, a, b);
			st.push(res);
		}

	}
}

TreeNode* addNode(int start, int end) {

	TreeNode* newNode = new TreeNode();
	int cnt = 0, idx;
	bool isLeft = true;

	if (start == end) {
		//숫자 체크
		if (input[start] != "(" && input[start] != ")") {
			error_check_num(input[start]);
		}

		newNode->node = input[start];
		newNode->LNode = NULL;
		newNode->RNode = NULL;
		return newNode;
	}
	//영어 체크
	if (input[start + 1] != "(" && input[start + 1] != ")") {
		error_check_str(input[start + 1]);
	}
	newNode->node = input[start + 1];


	if (end - start == 4) {
		newNode->LNode = addNode(start + 2, start + 2);
		newNode->RNode = addNode(start + 3, start + 3);
		return newNode;
	}


	for (int i = start + 2; i < end; i++) {
		if (i == start + 2 && isInt(input[i])) {
			newNode->LNode = addNode(i, i);

			isLeft = false;
		}
		else if (i == end - 1 && isInt(input[i])) {
			newNode->RNode = addNode(i, i);

		}

		if (input[i][0] == '(') {
			if (cnt == 0) {
				idx = i;
			}
			cnt++;
		}
		else if (input[i][0] == ')') {
			cnt--;
			if (cnt == 0) {
				if (isLeft) {
					newNode->LNode = addNode(start + 2, i);
					isLeft = false;
				}
				else {
					newNode->RNode = addNode(idx, i);
				}
			}
		}

	}

	return newNode;

}

void parse(char line[]) {
	string tmp = "";
	int opencnt = 0, closecnt = 0;

	for (int i = 0; line[i]; i++) {
		if (line[i] == '(') {
			input.push_back("(");
			opencnt++;
			tmp = "";
		}
		else if (line[i] == ')') {
			if (tmp.length()) {
				input.push_back(tmp);
			}
			input.push_back(")");
			closecnt++;
			tmp = "";
		}

		else if (line[i] == ' ') {
			if (tmp.length()) {
				input.push_back(tmp);
			}

			tmp = "";
		}
		else tmp += line[i];


	}
	if (tmp.length()) {
		input.push_back(tmp);
	}

	if (opencnt > closecnt) {
		error_msg.push_back(3);
	}

	else if (opencnt < closecnt) {
		error_msg.push_back(4);
	}
}

void operate(char line[]) {
	TreeNode* Root;

	input.clear();
	error_msg.clear();
	//parsing
	parse(line);

	//make tree
	Root = addNode(0, input.size() - 1);

	//error code 
	if (input.size() == 1) {
		if (isInt(input[0])) {
			cout << input[0] << endl;
			return;
		}
		else {
			cout << "undefined" << endl;
			return;
		}
	}
	if (error_msg.size() > 0) {
		for (int i = 0; i < error_msg.size(); i++) {
			cout << error_message[error_msg[i]] << endl;
		}
		return;
	}
	//postorder
	postorder(Root);

	//stack
	cout << "결과 : " << st.top() << endl;
	st.pop();
	return;

}

void interpreter() {
	ifstream ifile;
	char filename[200];
	char line[200];

	cout << "파일 명을 입력하세요 >> ";
	cin >> filename;
	/*
	수정 필요
	!!
	!!
	*/
	ifile.open(filename);

	if (ifile.is_open()) {
		cout << "파일 내용은 " << endl;
		cout << "-------------------------" << endl;
		while (ifile.getline(line, sizeof(line))) {
			cout << line << endl;
		}
		cout << "-------------------------" << endl;
		cout << "입니다." << endl << endl;

	}
	else {
		cout << "파일 읽기 오류 " << endl;

		return;
	}

	ifile.close();
	ifile.open(filename);

	if (ifile.is_open()) {
		while (ifile.getline(line, sizeof(line))) {
			operate(line);
		}
	}
	cout << endl;
	ifile.close();

	return;
}

void printDefun() {
	//defun list 출력

}
void defineDefun() {
	//line으로 입력 받기
	//엔터 누르면 정의된 defun list에 저장
}

void command() {

	int exe;
	cout << "=========================" << endl;
	cout << "1. Define DEFUN" << endl;
	cout << "2. Print DEFUN" << endl;
	cout << "3. Interpreter" << endl;
	cout << "4. Exit" << endl;
	cout << "=========================" << endl;
	cout << "메뉴를 선택하세요 >> ";
	cin >> exe;

	switch (exe) {

	case 1:
		//define defun
		defineDefun();
		break;

	case 2:
		//print defun
		printDefun();
		break;

	case 3:
		//interpreter
		interpreter();
		break;

	case 4:
		//exit
		cout << "프로그램을 종료합니다." << endl;
		exit(0);
		break;
	default:
		cout << "다시 입력해주세요." << endl;
	}

	return;

}
int main()
{
	while (1) {
		command();
	}


}

