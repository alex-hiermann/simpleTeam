package Utils;

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
    }

    @Test
    void extractTaskStateFromText() {
    }

    @Test
    void extractTaskDifficultyFromText() {
    }
}