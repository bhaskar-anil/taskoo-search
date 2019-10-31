package in.taskoo.search.task.controller;

import java.util.List;

import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import in.taskoo.search.task.document.Task;
import in.taskoo.search.task.repository.TaskSearchRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/{version}/search/tasks")
public class TaskSearchController {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskSearchController.class);
  
  private final ElasticsearchOperations elasticsearchOperations;

  private final TaskSearchRepository taskSearchRepository;

  
  @PostMapping("/")
  public void saveTask(@RequestBody Task task) {
    IndexQuery indexQuery = new IndexQueryBuilder()
                              .withId(task.getId().toString())
                              .withObject(task)
                              .build();
      String documentId = elasticsearchOperations.index(indexQuery);
      LOGGER.debug("task with id %s is saved.", documentId);
  }

  @GetMapping("/search")
  public @ResponseBody List<Task> search(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
      @RequestParam(required = false, defaultValue = "20") Integer pageSize, @RequestParam String query,
      @RequestParam(required = false) String... select) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    SourceFilter sourceFilter = new FetchSourceFilter(select, null);
    SearchQuery searchQuery = new NativeSearchQueryBuilder()
                                .withSourceFilter(sourceFilter)
                                .withPageable(pageable)
                                .withQuery(QueryBuilders.multiMatchQuery(query, "title", "description"))
                                .build();
    return taskSearchRepository.search(searchQuery).getContent();
  }

}
