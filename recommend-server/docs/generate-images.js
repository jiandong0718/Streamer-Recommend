/**
 * 架构图自动生成脚本
 * 使用Puppeteer自动截取架构图并保存为PNG图片
 * 
 * 使用方法：
 * 1. npm install puppeteer
 * 2. node generate-images.js
 */

const puppeteer = require('puppeteer');
const path = require('path');
const fs = require('fs');

// 配置
const config = {
    htmlFile: path.join(__dirname, '架构图表.html'),
    outputDir: path.join(__dirname, 'images'),
    viewport: { width: 1920, height: 1080 },
    delay: 3000, // 等待图表渲染的时间(毫秒)
};

// 图表配置
const diagrams = [
    {
        id: 'architecture-diagram',
        filename: 'system-architecture.png',
        description: '系统总体架构图'
    },
    {
        id: 'interaction-diagram', 
        filename: 'interaction-flow.png',
        description: '系统交互流程时序图'
    },
    {
        id: 'dataflow-diagram',
        filename: 'data-flow.png',
        description: '数据流图'
    },
    {
        id: 'deployment-diagram',
        filename: 'deployment-architecture.png',
        description: '部署架构图'
    }
];

/**
 * 创建输出目录
 */
function ensureOutputDir() {
    if (!fs.existsSync(config.outputDir)) {
        fs.mkdirSync(config.outputDir, { recursive: true });
        console.log(`✅ 创建输出目录: ${config.outputDir}`);
    }
}

/**
 * 生成单个图表
 */
async function generateDiagram(page, diagram) {
    try {
        console.log(`📊 开始生成: ${diagram.description}`);
        
        // 等待图表元素加载
        await page.waitForSelector(`#${diagram.id}`, { timeout: 10000 });
        
        // 额外等待确保Mermaid渲染完成
        await page.waitForTimeout(config.delay);
        
        // 获取图表元素
        const element = await page.$(`#${diagram.id}`);
        if (!element) {
            throw new Error(`找不到图表元素: #${diagram.id}`);
        }
        
        // 截取图表
        const outputPath = path.join(config.outputDir, diagram.filename);
        await element.screenshot({
            path: outputPath,
            type: 'png',
            omitBackground: false,
        });
        
        console.log(`✅ 生成成功: ${diagram.filename}`);
        return true;
        
    } catch (error) {
        console.error(`❌ 生成失败: ${diagram.description}`);
        console.error(`   错误信息: ${error.message}`);
        return false;
    }
}

/**
 * 主函数
 */
async function main() {
    console.log('🚀 开始生成架构图片...\n');
    
    // 检查HTML文件是否存在
    if (!fs.existsSync(config.htmlFile)) {
        console.error(`❌ HTML文件不存在: ${config.htmlFile}`);
        process.exit(1);
    }
    
    // 创建输出目录
    ensureOutputDir();
    
    let browser;
    try {
        // 启动浏览器
        console.log('🌐 启动浏览器...');
        browser = await puppeteer.launch({
            headless: 'new',
            args: [
                '--no-sandbox',
                '--disable-setuid-sandbox',
                '--disable-dev-shm-usage',
                '--disable-web-security',
                '--allow-file-access-from-files'
            ]
        });
        
        // 创建页面
        const page = await browser.newPage();
        await page.setViewport(config.viewport);
        
        // 加载HTML文件
        console.log('📄 加载HTML文件...');
        const fileUrl = `file://${config.htmlFile}`;
        await page.goto(fileUrl, { waitUntil: 'networkidle0' });
        
        // 等待Mermaid初始化
        console.log('⏳ 等待Mermaid图表渲染...');
        await page.waitForTimeout(config.delay);
        
        // 生成所有图表
        const results = [];
        for (const diagram of diagrams) {
            const success = await generateDiagram(page, diagram);
            results.push({ diagram: diagram.description, success });
        }
        
        // 输出结果统计
        console.log('\n📊 生成结果统计:');
        const successCount = results.filter(r => r.success).length;
        const totalCount = results.length;
        
        results.forEach(result => {
            const status = result.success ? '✅' : '❌';
            console.log(`   ${status} ${result.diagram}`);
        });
        
        console.log(`\n🎉 完成! 成功生成 ${successCount}/${totalCount} 个图表`);
        console.log(`📁 输出目录: ${config.outputDir}`);
        
        if (successCount < totalCount) {
            process.exit(1);
        }
        
    } catch (error) {
        console.error('❌ 生成过程中发生错误:');
        console.error(error);
        process.exit(1);
        
    } finally {
        if (browser) {
            await browser.close();
        }
    }
}

/**
 * 错误处理
 */
process.on('unhandledRejection', (error) => {
    console.error('❌ 未处理的Promise错误:');
    console.error(error);
    process.exit(1);
});

process.on('SIGINT', () => {
    console.log('\n🛑 用户中断操作');
    process.exit(0);
});

// 运行主函数
if (require.main === module) {
    main();
}

module.exports = { main, generateDiagram }; 