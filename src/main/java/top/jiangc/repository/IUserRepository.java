package top.jiangc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import top.jiangc.dao.IdataTemplate;
import top.jiangc.entity.User;

@Transactional
@Repository("userRepository")
public interface IUserRepository extends JpaRepository<User, String> {
	
	@Query("from user_account u where u.userId=:userId")
	public User getUserById(@Param("userId") String userId);
	
	public Page<User> findAll(Pageable pageable);

	@Query("from user_account u where u.userName=:userName and u.status <> 2")
	public User findUserByName(@Param("userName") String userName);

	@Query("from user_account u where u.userId=:userId")
	public User findUserById(@Param("userId") String userId);

}
