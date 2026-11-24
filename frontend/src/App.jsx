import {useState} from 'react'
import Dashboard from './components/Dashboard.jsx'
import Citizens from './components/Citizens.jsx'
import Grievances from './components/Grievances.jsx'
const items=[['dashboard','Dashboard'],['citizens','Citizens'],['grievances','Grievances'],['services','Services (M2)'],['welfare','Welfare (M3)'],['budget','Budget (M3)'],['reports','Reports (M4)']]
export default function App(){
 const [page,setPage]=useState('dashboard'),[focusId,setFocusId]=useState(null)
 const open=(id)=>{setFocusId(id);setPage('grievances')}
 return <div className="app">
  <header><b>CivicPulse Nexus</b><span>Milestone 1 · Citizen & Grievance Management</span><small>Municipal Admin</small></header>
  <div className="shell"><nav>{items.map(([key,label],i)=><button key={key} disabled={i>2} className={page===key?'active':''} onClick={()=>setPage(key)}>{label}</button>)}</nav>
   <main>{page==='dashboard'&&<Dashboard onOpen={open}/ >}{page==='citizens'&&<Citizens/>}{page==='grievances'&&<Grievances focusId={focusId}/>}</main>
  </div>
 </div>
}
