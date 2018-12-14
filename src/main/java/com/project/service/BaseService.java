package com.project.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.project.common.PageAjax;
import com.project.dao.BaseMapper;

/**
 * 通用业务层
 * 
 * @author Super
 */
public class BaseService<T> {

	@Autowired
	private BaseMapper<T> baseMapper;

	/**
	 * 通过ID查询
	 * 
	 * @param id
	 * @return
	 */
	public Object selectById(Serializable id) {
		return baseMapper.selectByPrimaryKey(id);
	}

	/**
	 * 查询单条记录
	 * 
	 * @param entity
	 * @return
	 */
	public Object selectOne(Object obj) {
		return baseMapper.selectOne(obj);
	}

	/**
	 * 查询集合
	 * 
	 * @param t
	 * @return
	 */
	public List<?> selectList(Object obj) {
		return (List<?>) baseMapper.selectList(obj);
	}

	/**
	 * 分页查询
	 * 
	 * @param t
	 * @param page
	 * @return
	 */
	public PageAjax<T> selectPage(Object obj, PageAjax<T> page) {
		List<T> list = baseMapper.selectPage(obj, page);
		return new PageAjax<T>(list);
	}

	/**
	 * 通用的保存方法
	 * 
	 * @param <T>
	 * @param entity
	 */
	public void save(Object obj) {
		baseMapper.insert(obj);
	}

	/**
	 * 批量保存
	 * 
	 * @param list
	 */
	public void batchSave(List<?> list) {
		baseMapper.batchSave(list);
	}

	/**
	 * 通用的修改方法
	 * 
	 * @param <T>
	 * @param entity
	 */
	public void update(Object obj) {
		baseMapper.updateByPrimaryKeySelective(obj);
	}

	/**
	 * 删除方法
	 * 
	 * @param id
	 */
	public int delById(Serializable id) {
		return baseMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 * 
	 * @param list
	 * @return
	 */
	public int delList(List<?> list) {
		return baseMapper.delList(list);
	}

	/**
	 * 批量删除方法
	 * 
	 * @param ids
	 */
	public int delArray(int[] ids) {
		return baseMapper.delArray(ids);
	}

	/**
	 * 统计查询
	 * 
	 * @param <T>
	 * @param params
	 *            查询参数
	 * @return 总记录条数
	 */
	public int count(T t) {
		return baseMapper.count(t);
	}

	/**
	 * 批量更新
	 * 
	 * @param list
	 * @return
	 */
	public int batchUpdate(List<?> list) {
		return baseMapper.batchUpdate(list);
	}
	/*
	 * @Autowired private SqlSessionTemplate sqlSessionTemplate;
	 *//**
	 * 批量更新
	 * 
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	/*
	 * public int batchUpdate(String str, List<?> objs) { SqlSessionFactory
	 * sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory(); // 批量执行器
	 * SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH,
	 * false); int succ = 0; try { if (objs != null) { for (int i = 0, size =
	 * objs.size(); i < size; i++) { sqlSession.update(str, objs.get(i)); }
	 * sqlSession.flushStatements(); sqlSession.commit();
	 * sqlSession.clearCache(); } } finally { sqlSession.close(); } return succ;
	 * }
	 */
}
