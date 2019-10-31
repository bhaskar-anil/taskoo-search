package in.taskoo.search.task.repository;

import org.springframework.data.repository.CrudRepository;

import in.taskoo.search.task.document.Task;

public interface TaskRepository extends CrudRepository<Task, String> {

}
