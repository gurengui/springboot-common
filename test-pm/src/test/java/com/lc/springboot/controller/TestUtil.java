package com.lc.springboot.controller;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lc.springboot.common.api.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.validation.constraints.NotNull;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 测试用例工具类
 *
 * @author: liangc
 * @date: 2020-08-20 09:07
 * @version 1.0
 */
@Slf4j
public class TestUtil<T> {

  public static final String UTF8 = "UTF-8";
  private static ObjectMapper objectMapper = new ObjectMapper();

  /**
   * 设置编码
   *
   * @param mvcResult
   */
  public static void setCharsetUTF8(MvcResult mvcResult) {
    mvcResult.getResponse().setCharacterEncoding(UTF8);
  }

  /**
   * 设置请求对象信息
   *
   * @param path 请求路径
   * @param paramObj 参数请求对象
   * @return {@link MockHttpServletRequestBuilder}
   * @throws JsonProcessingException
   */
  public BaseResponse postReq(MockMvc mockMvc, String path, Object paramObj) throws Exception {
    return getBaseResponse(mockMvc, path, paramObj, null);
  }

  /**
   * 设置请求对象信息
   *
   * @param path 请求路径
   * @param paramObj 参数请求对象
   * @param httpHeaders 请求头对象
   * @return {@link MockHttpServletRequestBuilder}
   * @throws JsonProcessingException
   */
  public BaseResponse postReq(
      MockMvc mockMvc, String path, Object paramObj, HttpHeaders httpHeaders) throws Exception {
    return getBaseResponse(mockMvc, path, paramObj, httpHeaders);
  }

  private BaseResponse getBaseResponse(
      MockMvc mockMvc, String path, Object paramObj, HttpHeaders httpHeaders) throws Exception {
    String resultContent = getResult(mockMvc, path, paramObj, httpHeaders);
    BaseResponse result = objectMapper.readValue(resultContent, BaseResponse.class);
    return result;
  }

  /**
   * 获取相应报文信息
   *
   * @param mockMvc mocMvc对象
   * @param path 请求路径
   * @param paramObj 参数对象
   * @param httpHeaders 请求头信息
   * @return 响应报文字符串信息
   * @throws Exception 异常信息
   */
  private String getResult(MockMvc mockMvc, String path, Object paramObj, HttpHeaders httpHeaders)
      throws Exception {
    MockHttpServletRequestBuilder postBuilder =
        getMockHttpServletRequestBuilder(path, paramObj, httpHeaders);

    MvcResult mvcResult = mockMvc.perform(postBuilder).andExpect(status().isOk()).andReturn();
    setCharsetUTF8(mvcResult);

    String resultContent = mvcResult.getResponse().getContentAsString();

    print(resultContent);
    return resultContent;
  }

  /**
   * 设置请求对象信息
   *
   * @param path 请求路径
   * @param paramObj 参数请求对象
   * @param clazz 封装内容泛型对象（info节点）
   * @return {@link MockHttpServletRequestBuilder}
   * @throws JsonProcessingException
   */
  public TestResponseBean<T> postReq(MockMvc mockMvc, String path, Object paramObj, Class<T> clazz)
      throws Exception {
    return getTestResponseBean(mockMvc, path, paramObj, null, clazz);
  }

  /**
   * 设置请求对象信息
   *
   * @param path 请求路径
   * @param paramObj 参数请求对象
   * @param httpHeaders 请求头信息
   * @return {@link MockHttpServletRequestBuilder}
   * @throws JsonProcessingException
   */
  public TestResponseBean<T> postReq(
      MockMvc mockMvc, String path, Object paramObj, HttpHeaders httpHeaders, Class<T> clazz)
      throws Exception {
    return getTestResponseBean(mockMvc, path, paramObj, httpHeaders, clazz);
  }

  private TestResponseBean<T> getTestResponseBean(
      MockMvc mockMvc, String path, Object paramObj, HttpHeaders httpHeaders, Class<T> clazz)
      throws Exception {
    String resultContent = getResult(mockMvc, path, paramObj, httpHeaders);
    JavaType javaType =
        objectMapper.getTypeFactory().constructParametricType(TestResponseBean.class, clazz);
    TestResponseBean<T> result = objectMapper.readValue(resultContent, javaType);

    return result;
  }

  /**
   * 格式化输出JSON字符串
   *
   * @return 格式化后的JSON字符串
   */
  private static String toPrettyFormat(String json) {
    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(jsonObject);
  }

  private static void print(String resultContent) {
    // 打印响应报文
    System.out.println("-----------------------------------------\n");
    System.out.println(toPrettyFormat(resultContent));
    System.out.println("\n------------------------------------------");
  }

  @NotNull
  private static MockHttpServletRequestBuilder getMockHttpServletRequestBuilder(
      String path, Object paramObj, HttpHeaders httpHeaders) throws JsonProcessingException {

    MockHttpServletRequestBuilder postBuilder =
        post(path).contentType(MediaType.APPLICATION_JSON).characterEncoding(UTF8);

    if (ObjectUtil.isNotEmpty(paramObj)) {
      postBuilder.content(objectMapper.writeValueAsBytes(paramObj));
    }
    if (httpHeaders != null) {
      postBuilder.headers(httpHeaders);
    }
    return postBuilder;
  }

  @NotNull
  private static MockHttpServletRequestBuilder getMockHttpServletRequestBuilder(
      String path, Object paramObj) throws JsonProcessingException {
    return getMockHttpServletRequestBuilder(path, paramObj, null);
  }
}