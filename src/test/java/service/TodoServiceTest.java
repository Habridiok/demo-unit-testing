package service;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import springboot.model.Todo;
import springboot.model.constants.TodoPriority;
import springboot.repository.TodoRepository;
import springboot.service.TodoService;

import java.util.ArrayList;
import java.util.List;

public class TodoServiceTest {

    //instatiate TodoService
    TodoService todoService;

    @Mock
    private TodoRepository todoRepository;

    @Before
    public void setUp() throws Exception{
//        this.todoRepository = new TodoReposi  tory();
        MockitoAnnotations.initMocks(this);
        this.todoService = new TodoService(this.todoRepository);
    }

    @After
    public void tearDown() throws Exception {
        Mockito.verifyNoMoreInteractions(todoRepository);
    }

    @Test
    public void getAllTest()throws Exception{

        List<Todo> todos = new ArrayList<Todo>();
        todos.add(new Todo("todo1", TodoPriority.MEDIUM));

        //given
        //todo repo  must return non empty list when getAll is called
        BDDMockito.given(todoRepository.getAll()).willReturn(todos);

        //when
        todos = todoService.getAll();

        //then
        //todo list not null
        Assert.assertThat(todos, org.hamcrest.Matchers.notNullValue());
        //todo list not empty
        Assert.assertThat(todos.isEmpty(),org.hamcrest.Matchers.equalTo(false));

        //verify
        BDDMockito.then(todoRepository).should().getAll();

    }

    @Test
    public void saveTodo(){
        String name ="dio";
        TodoPriority priority = TodoPriority.HIGH;

        //given
        BDDMockito.given(todoRepository.store(new Todo(name,priority))).willReturn(true);

        //when
        Boolean isInsertSuccess = todoService.saveTodo(name, priority);

        //then
        Assert.assertThat(name, Matchers.notNullValue());
        Assert.assertThat(priority, Matchers.notNullValue());
        Assert.assertThat(isInsertSuccess, Matchers.equalTo(true));

        //verify
        BDDMockito.then(todoRepository).should().store(new Todo(name,priority));

    }

}
