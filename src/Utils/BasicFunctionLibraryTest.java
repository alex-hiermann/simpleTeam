package Utils;

import Client.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasicFunctionLibraryTest {

    @org.junit.jupiter.api.Test
    void hashPassword() {
        assertEquals("35454B055CC325EA1AF2126E27707052", BasicFunctionLibrary.hashPassword("ILoveJava"));
        assertEquals("0cc175b9c0f1b6a831c399e269772661".toUpperCase(), BasicFunctionLibrary.hashPassword("a"));
        assertEquals("92eb5ffee6ae2fec3ad71c777531578f".toUpperCase(), BasicFunctionLibrary.hashPassword("b"));
    }


    @Test
    void findValueFromArgs() {
        String[] args = new String[]{"username=ꠦabcꠦ", "email=ꠦabc@abc.atꠦ", "userId=ꠦ2ꠦ", "messageText=ꠦHallo, mein Name ist abcꠦ"};
        assertEquals("abc", BasicFunctionLibrary.findValueFromArgs("username", args));
        assertEquals("abc@abc.at", BasicFunctionLibrary.findValueFromArgs("email", args));
        assertEquals(2, Integer.parseInt(BasicFunctionLibrary.findValueFromArgs("userId", args)));
        assertEquals("Hallo, mein Name ist abc", BasicFunctionLibrary.findValueFromArgs("messageText", args));
    }


    @Test
    void extractTaskTypeFromText() {
        assertEquals(Task.E_TASK_TYPE.TASK, BasicFunctionLibrary.extractTaskTypeFromText("TASK"));
        assertEquals(Task.E_TASK_TYPE.REMINDER, BasicFunctionLibrary.extractTaskTypeFromText("REMINDER"));
        assertEquals(Task.E_TASK_TYPE.MILESTONE, BasicFunctionLibrary.extractTaskTypeFromText("MILESTONE"));
    }

    @Test
    void extractTaskStateFromText() {
        assertEquals(Task.E_TASK_STATE.OPEN, BasicFunctionLibrary.extractTaskStateFromText("OPEN"));
        assertEquals(Task.E_TASK_STATE.STARTED, BasicFunctionLibrary.extractTaskStateFromText("STARTED"));
        assertEquals(Task.E_TASK_STATE.DUE, BasicFunctionLibrary.extractTaskStateFromText("DUE"));
        assertEquals(Task.E_TASK_STATE.FINISHED, BasicFunctionLibrary.extractTaskStateFromText("FINISHED"));
    }

    @Test
    void extractTaskDifficultyFromText() {
        assertEquals(Task.E_TASK_DIFFICULTY.EASY, BasicFunctionLibrary.extractTaskDifficultyFromText("EASY"));
        assertEquals(Task.E_TASK_DIFFICULTY.MEDIUM, BasicFunctionLibrary.extractTaskDifficultyFromText("MEDIUM"));
        assertEquals(Task.E_TASK_DIFFICULTY.HARD, BasicFunctionLibrary.extractTaskDifficultyFromText("HARD"));
        assertEquals(Task.E_TASK_DIFFICULTY.EXTREME, BasicFunctionLibrary.extractTaskDifficultyFromText("EXTREME"));
    }
}