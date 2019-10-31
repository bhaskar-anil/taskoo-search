package in.taskoo.search.task.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;

@Data
@Document(indexName = "task")
public class Task {
  @Id @Field(type = FieldType.Keyword) private String id;
  @Field(type = FieldType.Text) private String title;
  @Field(type = FieldType.Text) private String description;
}
