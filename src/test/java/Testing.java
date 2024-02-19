import org.junit.Test;
import static org.junit.Assert.*;
import org.json.JSONObject;

public class Testing {

    // some tests for locally testing methods in the server
    @Test
    public void typeWrong() {
        JSONObject req = new JSONObject();
        req.put("type1", "echo");

        JSONObject res = SockServer.testField(req, "type");

        assertEquals(res.getBoolean("ok"), false);
        assertEquals(res.getString("message"), "Field type does not exist in request");
    }

    @Test
    public void echoCorrect() {
        JSONObject req = new JSONObject();
        req.put("type", "echo");
        req.put("data", "whooooo");
        JSONObject res = SockServer.echo(req);

        assertEquals("echo", res.getString("type"));
        assertEquals(res.getBoolean("ok"), true);
        assertEquals(res.getString("echo"), "Here is your echo: whooooo");
    }

    @Test
    public void echoErrors() {
        JSONObject req = new JSONObject();
        req.put("type", "echo");
        req.put("data1", "whooooo");
        JSONObject res = SockServer.echo(req);

        assertEquals(res.getBoolean("ok"), false);
        assertEquals(res.getString("message"), "Field data does not exist in request");

        JSONObject req2 = new JSONObject();
        req2.put("type", "echo");
        req2.put("data", 33);
        JSONObject res2 = SockServer.echo(req2);

        assertEquals(false, res2.getBoolean("ok"));
        assertEquals(res2.getString("message"), "Field data needs to be of type: String");

        JSONObject req3 = new JSONObject();
        req3.put("type", "echo");
        req3.put("data", true);
        JSONObject res3 = SockServer.echo(req3);

        assertEquals(res3.getBoolean("ok"), false);
        assertEquals(res3.getString("message"), "Field data needs to be of type: String");
    }

     @Test
    public void storyboardAddSuccess() {
        JSONObject req = new JSONObject();
        req.put("type", "storyboard");
        req.put("view", false);
        req.put("name", "TestUser");
        req.put("story", "Once upon a time...");
        
        // Assuming SockServer.storyboardSentences and SockServer.storyboardUsers are public for test purposes
        SockServer.storyboardSentences.clear();
        SockServer.storyboardUsers.clear();

        JSONObject res = SockServer.storyboard(req);

        assertTrue(res.getBoolean("ok"));
        assertTrue(SockServer.storyboardSentences.contains("Once upon a time..."));
        assertTrue(SockServer.storyboardUsers.contains("TestUser"));
    }

    @Test
    public void storyboardAddDuplicateUser() {
        JSONObject req = new JSONObject();
        req.put("type", "storyboard");
        req.put("view", false);
        req.put("name", "TestUser");
        req.put("story", "Once upon a time...");

        SockServer.storyboardSentences.clear();
        SockServer.storyboardUsers.clear();
        SockServer.storyboard(req); // First addition

        // Try to add another story with the same username
        req.put("story", "And they lived happily ever after.");
        JSONObject res = SockServer.storyboard(req);

        assertFalse(res.getBoolean("ok"));
        assertEquals("This user has already added to the storyboard.", res.getString("message"));
    }

    @Test
    public void storyboardView() {
        JSONObject req = new JSONObject();
        req.put("type", "storyboard");
        req.put("view", true);

        SockServer.storyboardSentences.clear();
        SockServer.storyboardUsers.clear();
        SockServer.storyboardSentences.add("Once upon a time...");
        SockServer.storyboardUsers.add("TestUser");

        JSONObject res = SockServer.storyboard(req);

        assertTrue(res.getBoolean("ok"));
        assertEquals("Once upon a time...", res.getJSONArray("storyboard").getString(0));
        assertEquals("TestUser", res.getJSONArray("users").getString(0));
    }

  

    @Test
    public void testCharCountWithoutSpecificChar() {
        JSONObject req = new JSONObject();
        req.put("type", "charcount");
        req.put("findchar", false);
        req.put("count", "test");

        JSONObject res = SockServer.charCount(req);

        assertTrue(res.getBoolean("ok"));
        assertEquals(4, res.getInt("result"));
    }

    @Test
    public void testCharCountWithSpecificChar() {
        JSONObject req = new JSONObject();
        req.put("type", "charcount");
        req.put("findchar", true);
        req.put("find", "t");
        req.put("count", "testtest");

        JSONObject res = SockServer.charCount(req);

        assertTrue(res.getBoolean("ok"));
        assertEquals(4, res.getInt("result")); // 't' appears 4 times
    }

   
}