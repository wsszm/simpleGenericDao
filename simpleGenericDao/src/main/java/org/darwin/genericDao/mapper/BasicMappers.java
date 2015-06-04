/**
 * org.darwin.genericDao.dao.BaseMappers.java
 * created by Tianxin(tianjige@163.com) on 2015年5月28日 下午5:35:37
 */
package org.darwin.genericDao.mapper;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.darwin.common.utils.GenericDaoUtils;
import org.springframework.jdbc.core.RowMapper;

/**
 * 一些基础类的映射对象 created by Tianxin on 2015年5月28日 下午5:35:37
 */
public class BasicMappers {

	/**
	 * 将N个类各自的映射关系做一个map
	 */
	private static Map<Class<?>, Map<String, ColumnMapper>> columnMappersMap = new HashMap<Class<?>, Map<String,ColumnMapper>>(256);
	
	private static Map<Class<?>, Class<?>> entityKeyClassMap = new HashMap<Class<?>, Class<?>>();
	
	/**
	 * 获取一个实体类的主键类型
	 * @param entityClass
	 * @return
	 * created by Tianxin on 2015年6月3日 下午12:16:03
	 */
	public static Class<?> getKeyClass(Class<?> entityClass){
		return entityKeyClassMap.get(entityClass);
	}
	
	/**
	 * 将一个columnMappers注册到基础映射集中
	 * @param entityClass
	 * @param columnMappers
	 * created by Tianxin on 2015年6月1日 下午2:13:29
	 */
	public static void register(Class<?> keyClass, Class<?> entityClass, Map<String, ColumnMapper> columnMappers){
		entityKeyClassMap.put(entityClass, keyClass);
		columnMappersMap.put(entityClass, columnMappers);
	}
	
	/**
	 * 获取某个clazz的字段转载关系
	 * 
	 * @param clazz
	 * @return created by Tianxin on 2015年5月28日 下午5:56:17
	 */
	private static Map<String, ColumnMapper> getColumnMappers(Class<?> clazz) {
		return columnMappersMap.get(clazz);
	}

	/**
	 * 根据SQL和目标类，构造一个唯一的装载器
	 * 
	 * @param eClass
	 * @param sql
	 * @return created by Tianxin on 2015年5月28日 下午5:48:08
	 */
	public static <ENTITY extends Object> RowMapper<ENTITY> getEntityMapper(Class<ENTITY> eClass, String sql) {

		List<String> columns = GenericDaoUtils.getColumnsFromSQL(sql);
		Map<String, ColumnMapper> columnMappers = getColumnMappers(eClass);
		return new EntityMapper<ENTITY>(columns, columnMappers, eClass);
	}
	
	/**
	 * 获取一个基础类的mapper
	 * 
	 * @param rClass
	 * @return created by Tianxin on 2015年6月1日 下午1:49:42
	 */
	@SuppressWarnings("unchecked")
	public static <R> RowMapper<R> getMapper(Class<R> rClass) {
		
		//TODO 这里应该要进行map化处理，需要将类型判断做归一化处理

		if (Number.class.isAssignableFrom(rClass)) {
			if(Integer.class.isAssignableFrom(rClass)){
				return new RowMapper<R>() {
					public R mapRow(ResultSet rs, int rowNum) throws SQLException {
						return (R) Integer.valueOf(rs.getInt(0));
					}
				};
			}else if(Long.class.isAssignableFrom(rClass)){
				return new RowMapper<R>() {
					public R mapRow(ResultSet rs, int rowNum) throws SQLException {
						return (R) Long.valueOf(rs.getLong(0));
					}
				};
			}else if(Float.class.isAssignableFrom(rClass)){
				return new RowMapper<R>() {
					public R mapRow(ResultSet rs, int rowNum) throws SQLException {
						return (R) Float.valueOf(rs.getFloat(0));
					}
				};
			}else if(Double.class.isAssignableFrom(rClass)){
				return new RowMapper<R>() {
					public R mapRow(ResultSet rs, int rowNum) throws SQLException {
						return (R) Double.valueOf(rs.getDouble(0));
					}
				};
			}else if(Short.class.isAssignableFrom(rClass)){
				return new RowMapper<R>() {
					public R mapRow(ResultSet rs, int rowNum) throws SQLException {
						return (R) Short.valueOf(rs.getShort(0));
					}
				};
			}else if(Boolean.class.isAssignableFrom(rClass)){
				return new RowMapper<R>() {
					public R mapRow(ResultSet rs, int rowNum) throws SQLException {
						return (R) Boolean.valueOf(rs.getBoolean(0));
					}
				};
			}else if(Byte.class.isAssignableFrom(rClass)){
				return new RowMapper<R>() {
					public R mapRow(ResultSet rs, int rowNum) throws SQLException {
						return (R) Byte.valueOf(rs.getByte(0));
					}
				};
			}

		} else if (String.class.isAssignableFrom(rClass)) {
			return new RowMapper<R>() {
				public R mapRow(ResultSet rs, int rowNum) throws SQLException {
					return (R) rs.getString(0);
				}
			};
		} else if (java.util.Date.class.isAssignableFrom(rClass)) {
			if (Date.class.isAssignableFrom(rClass)) {
				return new RowMapper<R>() {
					public R mapRow(ResultSet rs, int rowNum) throws SQLException {
						return (R) rs.getDate(0);
					}
				};
			} else if (Timestamp.class.isAssignableFrom(rClass)) {
				return new RowMapper<R>() {
					public R mapRow(ResultSet rs, int rowNum) throws SQLException {
						return (R) rs.getTimestamp(0);
					}
				};
			} else if (Time.class.isAssignableFrom(rClass)) {
				return new RowMapper<R>() {
					public R mapRow(ResultSet rs, int rowNum) throws SQLException {
						return (R) rs.getTime(0);
					}
				};
			}
		} else if(BigDecimal.class.isAssignableFrom(rClass)){
			return  new RowMapper<R>() {
				public R mapRow(ResultSet rs, int rowNum) throws SQLException {
					return (R) rs.getBigDecimal(0);
				}
			};
		} else if(URL.class.isAssignableFrom(rClass)){
			return  new RowMapper<R>() {
				public R mapRow(ResultSet rs, int rowNum) throws SQLException {
					return (R) rs.getURL(0);
				}
			};
		} else if(Blob.class.isAssignableFrom(rClass)){
			return  new RowMapper<R>() {
				public R mapRow(ResultSet rs, int rowNum) throws SQLException {
					return (R) rs.getBlob(0);
				}
			};
		} else if(Clob.class.isAssignableFrom(rClass)){
			return  new RowMapper<R>() {
				public R mapRow(ResultSet rs, int rowNum) throws SQLException {
					return (R) rs.getClob(0);
				}
			};
		}

		return new RowMapper<R>() {
			public R mapRow(ResultSet rs, int rowNum) throws SQLException {
				return (R) rs.getObject(0);
			}
		};
	}

}
