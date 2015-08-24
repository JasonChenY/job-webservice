package com.tiaonaer.ws.todo;

import com.tiaonaer.ws.todo.document.TodoDocument;
import com.tiaonaer.ws.todo.dto.TodoDTO;
import com.tiaonaer.ws.todo.model.Todo;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Petri Kainulainen
 */
public class TodoTestUtil {

    public static final Long ID = 1L;
    public static final String DESCRIPTION = "description";
    public static final String DESCRIPTION_UPDATED = "updatedDescription";
    public static final String TITLE = "title";
    public static final String TITLE_UPDATED = "updatedTitle";

    private static final String CHARACTER = "a";

    public static TodoDocument createDocument(Long id, String description, String title) {
        return TodoDocument.getBuilder(id, title)
                .description(description)
                .build();
    }

    public static TodoDTO createDTO(Long id, String description, String title) {
        TodoDTO dto = new TodoDTO();

        dto.setId(id);
        dto.setDescription(description);
        dto.setTitle(title);

        return dto;
    }

    public static Todo createModel(Long id, String description, String title) {
        Todo model = Todo.getBuilder(title)
                .description(description)
                .build();

        ReflectionTestUtils.setField(model, "id", id);

        return model;
    }

    public static String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();

        for (int index = 0; index < length; index++) {
            builder.append(CHARACTER);
        }

        return builder.toString();
    }
}
