package controller;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
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
import springboot.service.TodoService;
import springboot.model.request.CreateTodoRequest;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Introduction.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoControllerTest {

    @MockBean
    private TodoService todoService;

    @LocalServerPort
    private int serverPort;

    @Before
    public void setUp(){

    }

    @After
    public void tearDown(){
        Mockito.verifyNoMoreInteractions(todoService);
    }

    private static final String name = "todo1";
    private static final TodoPriority priority = TodoPriority.HIGH;

    private static final String TODO ="{\"code\":200,\"message\":null,\"value\":[{\"name\":\"todo1\",\"priority\":\"HIGH\"}]}";

    @Test
    public void getAllTest() throws Exception {
        //given
        when(todoService.getAll()).thenReturn(Arrays.asList(new Todo(name, priority)));

//        List<Todo> result = new ArrayList<Todo>();
//        result.add(new Todo("todo1", TodoPriority.MEDIUM));
//        BDDMockito.given(todoService.getAll()).willReturn(result);

        //when
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .port(serverPort)
                .get("/todos")
                .then()
                .body(containsString("value"))
                .body(containsString(name))
                .body(Matchers.equalTo(TODO))
                .statusCode(200);

        //then
        BDDMockito.then(todoService).should().getAll();
    }

    @Test
    public void insertTest(){

        CreateTodoRequest createTodoRequest = new CreateTodoRequest();
        createTodoRequest.setName(name);
        createTodoRequest.setPriority(priority);

        BDDMockito.when(todoService.saveTodo(createTodoRequest.getName(), createTodoRequest.getPriority())).thenReturn(true);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(createTodoRequest)
                .when()
                .port(serverPort)
                .post("/todos");

        Mockito.verify(todoService).saveTodo(name, priority);
    }

}
