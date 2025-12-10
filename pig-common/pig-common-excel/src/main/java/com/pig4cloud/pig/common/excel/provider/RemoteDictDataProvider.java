package com.pig4cloud.pig.common.excel.provider;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.pig4cloud.pig.common.core.util.Result;
import com.pig4cloud.plugin.excel.handler.DictDataProvider;
import com.pig4cloud.plugin.excel.vo.DictEnum;

import java.util.List;
import java.util.Map;

/**
 * 远程字典数据提供程序实现类
 */
public class RemoteDictDataProvider implements DictDataProvider {

    private final RemoteDictApiService remoteDictApiService;

    public RemoteDictDataProvider(RemoteDictApiService remoteDictApiService) {
        this.remoteDictApiService = remoteDictApiService;
    }

    /**
     * 根据类型获取字典枚举数组
     */
    @Override
    public DictEnum[] getDict(String type) {
        Result<List<Map<String, Object>>> dictDataListR = remoteDictApiService.getDictByType(type);
        List<Map<String, Object>> dictDataList = dictDataListR.getData();
        if (CollUtil.isEmpty(dictDataList)) {
            return new DictEnum[0];
        }

        // 构建 DictEnum 数组
        DictEnum.Builder dictEnumBuilder = DictEnum.builder();
        for (Map<String, Object> dictData : dictDataList) {
            String value = MapUtil.getStr(dictData, "value");
            String label = MapUtil.getStr(dictData, "label");
            dictEnumBuilder.add(value, label);
        }

        return dictEnumBuilder.build();
    }

}
