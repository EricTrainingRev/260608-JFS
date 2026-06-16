package com.theblind.todo.Repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.theblind.todo.Entity.Task;

@Repository
public interface TaskRepo extends JpaRepository<Task, UUID> {
    List<Task> findByUserId(Integer userId);
    List<Task> findByParentTaskId(Integer parentTaskId);

    @Query(
        nativeQuery = true,
        value = """
                WITH RECURSIVE Children AS (
                    SELECT id FROM tasks WHERE id = :id

                    UNION ALL

                    SELECT child.id
                    FROM tasks child
                    INNER JOIN Children parent ON child.parent_task_id = parent.id
                )
                
                DELETE FROM Children
                """
    )
    void deleteChildren(UUID id);
}
