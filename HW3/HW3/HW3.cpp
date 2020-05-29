#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <stack>
#include <malloc.h>

using namespace std;

vector<string> input;
stack<string> st;
vector<int> error_msg;
vector<struct defun> defun_list;

//defun 자료구조를 저장할 struct
typedef struct defun {
	string keyword;				//함수명
	vector<string> param;		//매개변수 리스트
	vector<string> expression; //수식
}defun;

//이진트리를 위한 struct
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

//정수형인지 아닌지 판별
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

//defun list에서 str과 일치하는 keyword가 존재하는지 검사
int findKeyword(string str) {

	for (int i = 0; i < defun_list.size(); i++) {
		if (defun_list[i].keyword == str) {
			return i;
		}
	}

	return 777;
}

//숫자인지 아닌지 판별
void error_check_num(string check) {
	for (int i = 0; i < check.length(); i++) {
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

//문자열이 MINUS, IF 제외 다른게 있는지 확인
void error_check_str(string check) {
	if (check.compare("MINUS") != 0 && check.compare("IF") != 0) {
		error_msg.push_back(2);
	}
}

//IF, MINUS연산
string calculate(string op, string a, string b) {

	int a1 = stoi(a);
	int b1 = stoi(b);

	if (op[0] == 'M') {
		return to_string((a1 - b1));
	}
	else if (op[0] == 'I') {
		if (a1 > 0) {
			return to_string(b1);
		}
		else {
			return to_string(0);
		}
	}
}

//후위 트리 순회
int postorder(TreeNode* ptr)
{  
	if (ptr) {

		postorder(ptr->LNode);
		postorder(ptr->RNode);
		
		//노드를 스택에 삽입
		st.push(ptr->node);
		
		cout << st.top() << " ";
		//MINUS, IF를 만나면 pop후, 연산 결과 push
		if (ptr->node == "MINUS" || ptr->node == "IF") {
			
			if (st.size() < 3) {
				cout << "수식이 잘못되었습니다" << endl;
				while (false == st.empty()){
					st.pop();
				}
				return 777;
			}
			
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

//노드 삽입 함수
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

	//재귀적으로 수행
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

//input에서 MINUS, IF 제외한 함수 있을 경우, 그 함수의 expression으로 변환해준다
vector<string> substitute( vector<pair<int, int>> param_list, int idx) {
	
	vector<string> tmp;
	string cur = "";
	vector<string> change;
	tmp = defun_list[idx].expression;

	for (int i = 0; i < defun_list[idx].param.size(); i++) {
		
		cur = defun_list[idx].param[i];
		change.resize(param_list[i].second - param_list[i].first + 1);
		
		copy(input.begin() + param_list[i].first, input.begin() + param_list[i].second+1, change.begin());
		

		for (int j = 0; j < tmp.size(); j++) {
			//defun struct의 param과 tmp[j] 위치가 같을때 (매개변수 위치 찾음)
			if (cur == tmp[j]) {
				int a = j;
				tmp.erase(tmp.begin() + j);
				//tmp[j] 위치에 input[param[i].first] 부터 input[param[i].second] 까지 삽입
				for (int k = param_list[i].first; k <= param_list[i].second; k++) {
					tmp.insert(tmp.begin() + a, input[k]);
					a++;
					j++;
				}

			}

		}
	}

	return tmp;
}

//IF, MINUS, (, ) 제외 다른게 있는지 확인. 있으면 substitute(). 
int func() {
	int flag = 0;

	for (int i = 0; i < input.size(); i++) {
	
		if (input[i] == "(" || input[i] == ")") {
			continue;
		}
		if (input[i] == "MINUS" || input[i] == "IF") {
			continue;
		}
		if (isInt(input[i])) {
			continue;
		}
		else {
			flag = 1;
			//함수 존재하는지 확인
			int keyIdx = findKeyword(input[i]);
			if (keyIdx == 777) {
				cout << "존재하지 않는 함수명 입니다" << endl;
				return 777;
			}
			
			//param_list 만들기
			vector<pair<int, int>> param_list;
			
			int opencnt = 0, closecnt = 0;
			int startIdx = -1, endIdx = -1; 
			int flag = 0;
			for (int j = i + 1; j < input.size(); j++) {
				
				if (isInt(input[j]) == true && flag == 0) {
					param_list.push_back(make_pair(j, j));
					if (param_list.size() == defun_list[keyIdx].param.size()) {
						break;
					}
					else {
						continue;
					}
				}
				else {
					if (input[j] == "(") {
						flag = 1;
						if (startIdx == -1) {
							startIdx = j;
						}
						opencnt++;
					}
					else if (input[j] == ")") {
						closecnt++;

						if (opencnt == closecnt) {
							endIdx = j;
							param_list.push_back(make_pair(startIdx, endIdx));
							startIdx = -1;
							endIdx = -1;
							flag = 0;
							if (param_list.size() == defun_list[keyIdx].param.size()) {
								break;
							}
						}
					}
				}
			}
			if (param_list.size() != defun_list[keyIdx].param.size()) {
				cout << "매개변수 개수가 잘못되었습니다" << endl;
				return 777;
			}
			/* input의 i -1 ~ param_list[param_list.size()-1].second + 1 까지 
			 * (ex : (ADD 4 5) 부분에서 '(', ')' 위치)
			 * 치환하기(i-1부터 계산한 마지막 인덱스 + 1) 
			 */
			vector<string> tmp = substitute(param_list, keyIdx);

			int a = i - 1;
			for (int j = i - 1; j <= param_list[param_list.size() - 1].second + 1; j++) {
				input.erase(input.begin() + a);
			}

			for (int j = 0; j < tmp.size(); j++){
				input.insert(input.begin() + a, tmp[j]);
				a++;
				i++;
			}
			}
	}

	return flag;
}

//char[] 문자열을 나누어 string vector인 input에 삽입
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

//연산 수행 총괄 함수
void operate(char line[]) {
	TreeNode* Root;

	input.clear();
	error_msg.clear();
	
	//parsing
	parse(line);

	for (int i = 0; i < input.size(); i++) {
		cout << input[i] << " ";
	}

	//한 단어만 입력했을 경우 
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

	cout << " -> ";

	//defun 함수 존재하면 바꾸어줌
	do {
		func();
		if (func() == 777) {
			return;
		}
	} while (func() == 1);

	//결과 출력
	for (int i = 0; i < input.size(); i++) {
		cout << input[i] << " ";
	}
	cout << endl;

	//make tree
	Root = addNode(0, input.size() - 1);

	//에러사항이 존재하면, 에러메세지 출력 후 operate()함수 종료
	if (error_msg.size() > 0) {
		for (int i = 0; i < error_msg.size(); i++) {
			cout << error_message[error_msg[i]] << endl;
		}
		return;
	}

	//postorder
	cout << "Prefix to Postfix : ";
	int res = postorder(Root);
	if (res == 777) {
		return;
	}

	//stack의 top 출력
	cout << "\nresult : " << st.top() << endl;
	st.pop();
	return;

}

//interprete 모드
void interpreter() {

	ifstream ifile;
	char filename[200];
	char line[200];

	cout << "실행 파일 명을 입력하세요 >> ";
	cin >> filename;
	
	ifile.open(filename);

	if (ifile.is_open()) {
		while (ifile.getline(line, sizeof(line))) {
			cout << "\n***********************************************\n" << endl;
			operate(line);
		}
	}
	else {
		cout << "파일 읽기 오류 " << endl;

		return;
	}


	cout << endl;
	ifile.close();

	return;
}

//defun list 출력
void printDefun() {
	for (int i = 0; i < defun_list.size(); i++) {

		cout << defun_list[i].keyword;

		cout << " ( ";
		for (int j = 0; j < defun_list[i].param.size(); j++) {
			cout << defun_list[i].param[j] << " ";
		}
		cout << ") ";

		for (int j = 0; j < defun_list[i].expression.size(); j++) {
			cout << defun_list[i].expression[j] << " ";
		}
		cout << endl;
	}


}

//defun 정의
void defineDefun() {
	//line으로 입력 받기
	string line;
	defun* newDefun = new defun();

	int a = getchar();
	getline(cin, line);
	
	if (line.size() < 6 || line.find("DEFUN") == string::npos) {
		cout << "DEFUN을 포함하여 입력해 주세요. \n예 : DEFUN POS (v4) (IF v4 1)" << endl;
		return;
	}
	line = line.substr(6, line.size()-1);
	string tmp;
	int idx = 0;
	
	for (int i = 0; i < line.size(); i++) {
		//keyword 저장
		if (idx == 0 && line[i] == ' ') {
			newDefun ->keyword = tmp;
			tmp = "";
			idx = i;	
			continue;
		}
		
		//매개변수 저장
		if (idx != 0 && line[i] == ')') {
			string param = "";
			for (int j = idx+2; j <= i; j++) {
				if (line[j] == ' ' || line[j] == ')') {
					if (param != "") {
						newDefun->param.push_back(param);
						param = "";
					}
				}
				else {
					param += line[j];
				}
			}
			i += 2;
			idx = i;
			tmp = "";
			break;
		}
		tmp += line[i];
	}

	//수식 저장
	//길이가 idx보다 길어야만 substr 가능
	if (line.size() < idx) {
		cout << "수식이 잘못 되었습니다" << endl;
		return;
	}

	line = line.substr(idx, line.size() - 1);
	int len = line.length() + 1;
	char* cstr = new char[len];



	strcpy(cstr, line.c_str());

	parse(cstr);
	for (int i = 0; i < input.size(); i++) {
		newDefun->expression.push_back(input[i]);
	}
	input.clear();


	//이미 있는 함수인지 검사
	for (int i = 0; i < defun_list.size(); i++) {
		if (newDefun->keyword == defun_list[i].keyword) {
			cout << "이미 존재하는 함수 입니다." << endl;
			return;
		}
	}

	//정의된 defun list에 저장
	defun_list.push_back(*newDefun);
	
}

void command() {

	int exe;
	cout << "===============================================" << endl;
	cout << "1. Define DEFUN" << endl;
	cout << "2. Print DEFUN" << endl;
	cout << "3. Interpreter" << endl;
	cout << "4. Exit" << endl;
	cout << "===============================================" << endl;
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
	//while (getchar() != '\n');

	return;

}
int main()
{
	while (1) {
		command();
	}


}

