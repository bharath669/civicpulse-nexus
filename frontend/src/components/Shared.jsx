export function ErrorBox({error}){
 if(!error)return null
 const fields=Object.entries(error.fieldErrors||{})
 return <div className="error"><b>{error.message}</b>{fields.length>0&&<ul>{fields.map(([k,v])=><li key={k}><b>{k}</b>: {v}</li>)}</ul>}</div>
}
export function Status({value}){return <span className={`badge st-${value}`}>{value?.replaceAll('_',' ')}</span>}
export function Sla({value}){return <span className={`badge sla-${value}`}>{value?.replaceAll('_',' ')}</span>}
export function Stat({label,value,hint,tone=''}){return <div className={`stat ${tone}`}><small>{label}</small><strong>{value}</strong><small>{hint}</small></div>}
