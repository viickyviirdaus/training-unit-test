package service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import springboot.model.Todo;
import springboot.model.constants.TodoPriority;
import springboot.repository.TodoRepository;
import springboot.service.TodoService;

import java.util.ArrayList;
import java.util.List;

public class ToDoServiceTest {


    //instatntiate ToDoService
    private TodoService todoService;

    @Mock
    private TodoRepository todoRepository;

    @Before
    public void setUp() throws Exception{
     //   this.todoRepository = new TodoRepository(); diganti sama @Mock

        MockitoAnnotations.initMocks(this);
        this.todoService = new TodoService(this.todoRepository);

    }

    @After
    public void tearsDown() throws Exception{

        //dipanggil dietiap method yang selesai di tes
        //verivi jika tidak ada interaksi lain
        Mockito.verifyNoMoreInteractions(todoRepository);
    }

    @Test
    public void getAllTest() throws Exception{
        //save, buat ngisi
        //todoService.saveTodo("Todo1", TodoPriority.HIGH);

        //given,
        //todo repo must return non empty list when getAll is called
        //willThrow(new RuntimeException("boo")).given(mock).foo();

        ArrayList<Todo> todos = new ArrayList<Todo>();
        todos.add(new Todo("todo1", TodoPriority.MEDIUM));

        BDDMockito.given(todoRepository.getAll()).willReturn(todos);

        //when
        List<Todo> todoList = todoService.getAll();
        //then
        //assertThat(goods, containBread());
        //assert that todo list is not null
        Assert.assertThat(todoList, org.hamcrest.Matchers.notNullValue());

        //assert that todo list is not empty
        Assert.assertThat(todoList.isEmpty(), org.hamcrest.Matchers.equalTo(false));

        //prep data
        //todoRepository.store(new Todo("todo1", TodoPriority.MEDIUM));

        //call getAll
        //List<Todo> todoList = todoService.getAll();
        //check the response

        //verivy, jika apa yang di panggil di method getAll tdak benar" berasal dari repositori
        BDDMockito.then(todoRepository).should().getAll();

        if (todoList.isEmpty()){
            throw new Exception("Kosong");
        }else {
            System.out.println("Tak Kosong");
        }

    }


        @Test
        public void saveTodoTest() throws Exception{

            //given
            BDDMockito.given(todoRepository.store(new Todo("todo2", TodoPriority.MEDIUM))).willReturn(true);

            //when
            boolean result = todoService.saveTodo("todo2", TodoPriority.MEDIUM);
            //then
            //assert that todo list is not null
            Assert.assertThat(result, org.hamcrest.Matchers.notNullValue());

            //assert that todo list is not empty
            Assert.assertThat(result, org.hamcrest.Matchers.equalTo(true));
//
            //verivy, jika apa yang di panggil di method store tdak benar" berasal dari repositori
            BDDMockito.then(todoRepository).should().store(new Todo("todo2", TodoPriority.MEDIUM));



        }
}
