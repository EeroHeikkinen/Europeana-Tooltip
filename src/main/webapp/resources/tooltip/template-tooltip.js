    	<div class="wowhead-tooltip">
<div class="" style="position:absolute; left: -60px; visibility: visible;">    <div>
	<img width="44" height="44" src="${fields["europeana:object"]}"></div>   </div>
		<table>
			<tbody>
				<tr>
					<td><table style="white-space: nowrap; width: 100%;">
							<tbody>
								<tr>
									<td><b class="q3">${fields["dc:title"]}</b><br>
									<!--bo-->
									<table style="width: 100%;">
									<tbody>
										<tr>
											<td>
												<!--rr-->
												<span class="q2">${fields["dc:description"]}</span>
													<br />
											</td>
										</tr>
									</tbody>
								</table>
										<table width="100%">
											<tbody>
												<tr>
													<td>Type: ${fields["dc:type"]}</td>
													<th>$ {fields.display.lds05}</th>
												</tr>
											</tbody>
										</table>
										<!--rf-->
										<span>$ {localization['field.creationdate']}: $ {fields.display.creationdate}</span>
<br>$ {localization['field.identifier']}: $ {fields.display.identifier}<br><br>

</td>
								</tr>
							</tbody> 
						</table>
					</td>
					<th style="background-position: 100% 0%;"></th>
				</tr>
				<tr>
					<th style="background-position: 0% 100%;"></th>
					<th style="background-position: 100% 100%;"></th>
				</tr>
			</tbody>
		</table>
	</div>