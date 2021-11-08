#include <jni.h>
#include <string>

char board[3][3] = {{'1','2','3'},{'4','5','6'},{'7','8','9'}};
char turn = 'X';
bool draw = false;

bool checkContinue(){
    // check row
    for(int i=0; i<3; i++)
        if(board[i][0] == board[i][1] && board[i][0] == board[i][2]
        || board[0][i] == board[1][i] && board[0][i] == board[2][i])
            return false;

    //checking the win for both diagonal
    if(board[0][0] == board[1][1] && board[0][0] == board[2][2] || board[0][2] == board[1][1] && board[0][2] == board[2][0])
        return false;

    //Checking the game is in continue mode or not
    for(int i=0; i<3; i++)
        for(int j=0; j<3; j++)
            if(board[i][j] != 'X' && board[i][j] != 'O')
                return true;

    //Checking the if game already draw
    draw = true;
    return false;
}

bool checkIsInvalidMove(int row, int column) {
    char boardValue = board[row][column];
    bool invalid = false;
    switch (boardValue) {
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9': break;
        default: invalid = true;
    }
    return invalid;
}

void placeBoard(int row, int column) {
    if (turn == 'X') {
        board[row][column] = 'X';
    } else {
        board[row][column] = 'O';
    }
}

void changePlayer() {
    if (turn == 'X') {
        turn = 'O';
    } else {
        turn = 'X';
    }
}

void updateBoard(JNIEnv* env, jobject wrapper) {
    jcharArray resultBoard;
    int size = 9;
    resultBoard = env->NewCharArray(size);
    if(resultBoard == nullptr) {
        return;
    }
    int position = 0;
    jchar temp[size];
    for (int row = 0; row < 3; row++) {
        for(int column = 0; column < 3; column++) {
            if(board[row][column] != 'X' && board[row][column] != 'O') {
                temp[position] = ' ';
            } else {
                temp[position] = board[row][column];
            }
            position++;
        }
    }
    env->SetCharArrayRegion(resultBoard, 0, size, temp);

    jclass nativeWrapper = env->FindClass("com/after/thursday/xo/XOLibraryWrapper");
    jmethodID  methodId = env->GetMethodID(nativeWrapper, "onUpdateBoard", "([C)V");
    env->CallVoidMethod(wrapper, methodId, resultBoard);
}

void updatePlayer(JNIEnv* env, jobject wrapper) {
    const char *b = &turn;
    jstring player = env->NewStringUTF(b);
    jclass nativeWrapper = env->FindClass("com/after/thursday/xo/XOLibraryWrapper");
    jmethodID  methodId = env->GetMethodID(nativeWrapper, "onUpdateTurn", "(Ljava/lang/String;)V");
    env->CallVoidMethod(wrapper, methodId, player);
}

void updateWinner(JNIEnv* env, jobject wrapper) {
    const char *b = &turn;
    jstring player = env->NewStringUTF(b);
    jclass nativeWrapper = env->FindClass("com/after/thursday/xo/XOLibraryWrapper");
    jmethodID  methodId = env->GetMethodID(nativeWrapper, "onWin", "(Ljava/lang/String;)V");
    env->CallVoidMethod(wrapper, methodId, player);
}


void updateDraw(JNIEnv* env, jobject wrapper) {
    jclass nativeWrapper = env->FindClass("com/after/thursday/xo/XOLibraryWrapper");
    jmethodID  methodId = env->GetMethodID(nativeWrapper, "onDraw", "()V");
    env->CallVoidMethod(wrapper, methodId);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_after_thursday_xo_XOLibraryWrapper_placeXO(
        JNIEnv* env,
        jobject wrapper,
        jint row,
        jint column) {

    if(checkIsInvalidMove(row, column)) {
        return false;
    }

    placeBoard(row, column);

    if(checkContinue()) {
        changePlayer();
        updatePlayer(env, wrapper);
        updateBoard(env, wrapper);
    } else {
        updateBoard(env, wrapper);
        if(draw) {
            updateDraw(env, wrapper);
        } else {
            updateWinner(env, wrapper);
        }
    }
    return true;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_after_thursday_xo_XOLibraryWrapper_resetGame(
        JNIEnv* env,
        jobject wrapper) {
    // reset board
    int index = 1;
    for(int row = 0; row < 3; row++) {
        for(int column = 0; column < 3; column++) {
            board[row][column] = std::to_string(index)[0];
            index++;
        }
    }
    turn = 'X';
    draw = false;
    updateBoard(env, wrapper);
    return true;
}

