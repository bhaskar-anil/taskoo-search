package in.taskoo.search.task.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import in.taskoo.search.task.document.Task;

@Repository
public interface TaskSearchRepository extends ElasticsearchRepository<Task,Long> {

}
