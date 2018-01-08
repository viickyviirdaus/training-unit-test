package controller;

import com.jayway.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import springboot.Introduction;
import springboot.model.Todo;
import springboot.model.constants.TodoPriority;
import springboot.model.request.CreateTodoRequest;
import springboot.service.TodoService;

import java.util.Arrays;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Introduction.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoControllerTest {

    @MockBean
    private TodoService todoService;

    @LocalServerPort
    private int serverPort;

    private static final String NAME = "Todo1";
    private static final TodoPriority PRIORITY = TodoPriority.MEDIUM;
    private static final String TODO = "{\"code\":200,\"message\":null,\"value\":[{\"name\":\"Todo1\",\"priority\":\"MEDIUM\"}]}";

    @Test
    public void all(){

        when(todoService.getAll()).thenReturn(Arrays.asList(new Todo(NAME, PRIORITY)));

        RestAssured.given().contentType("application/json")
                .when()
                .port(serverPort)
                .get("/todos")
                .then()
                .body(Matchers.containsString("value"))
                .body(Matchers.containsString(NAME))
                .body(equalTo(TODO))
                .statusCode(200);
        verify(todoService).getAll();
    }

    @Test
    public void insertTest(){
        CreateTodoRequest createTodoRequest = new CreateTodoRequest();
        createTodoRequest.setName(NAME);
        createTodoRequest.setPriority(PRIORITY);

        BDDMockito.when(todoService.saveTodo(createTodoRequest.getName(), createTodoRequest.getPriority())).thenReturn(true);

        RestAssured.given()
                .contentType("application/json")
                .body(createTodoRequest)
                .when()
                .port(serverPort)
                .post("/todos");

        Mockito.verify(todoService).saveTodo(NAME, PRIORITY);

    }

    @After

    public void tearDown() {
        verifyNoMoreInteractions(this.todoService);
    }
}
