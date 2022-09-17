import defaultSettings from '@/settings'

const title = defaultSettings.title || '社区疫情资源管理系统'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
