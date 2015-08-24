package com.tiaonaer.ws.todo.document;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;

/**
 * @author Petri Kainulainen
 */
public class TodoDocument {

    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_ID = "id";
    public static final String FIELD_TITLE = "title";

    @Id
    @Field
    private String id;

    @Field
    private String description;

    @Field
    private String title;

    public TodoDocument() {

    }

    public static Builder getBuilder(Long id, String title) {
        return new Builder(id, title);
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public static class Builder {
        private TodoDocument build;

        public Builder(Long id, String title) {
            build = new TodoDocument();
            build.id = id.toString();
            build.title = title;
        }

        public Builder description(String description) {
            build.description = description;
            return this;
        }

        public TodoDocument build() {
            return build;
        }
    }
}
